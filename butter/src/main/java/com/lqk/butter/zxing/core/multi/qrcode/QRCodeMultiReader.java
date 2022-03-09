/*
 * Copyright 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lqk.butter.zxing.core.multi.qrcode;

import com.lqk.butter.zxing.core.BarcodeFormat;
import com.lqk.butter.zxing.core.BinaryBitmap;
import com.lqk.butter.zxing.core.DecodeHintType;
import com.lqk.butter.zxing.core.NotFoundException;
import com.lqk.butter.zxing.core.ReaderException;
import com.lqk.butter.zxing.core.Result;
import com.lqk.butter.zxing.core.ResultMetadataType;
import com.lqk.butter.zxing.core.ResultPoint;
import com.lqk.butter.zxing.core.common.DecoderResult;
import com.lqk.butter.zxing.core.common.DetectorResult;
import com.lqk.butter.zxing.core.multi.MultipleBarcodeReader;
import com.lqk.butter.zxing.core.multi.qrcode.detector.MultiDetector;
import com.lqk.butter.zxing.core.qrcode.QRCodeReader;
import com.lqk.butter.zxing.core.qrcode.decoder.QRCodeDecoderMetaData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

/**
 * This implementation can detect and decode multiple QR Codes in an image.
 *
 * @author Sean Owen
 * @author Hannes Erven
 */
public final class QRCodeMultiReader extends QRCodeReader implements MultipleBarcodeReader {

  private static final Result[] EMPTY_RESULT_ARRAY = new Result[0];
  private static final ResultPoint[] NO_POINTS = new ResultPoint[0];

  @Override
  public Result[] decodeMultiple(BinaryBitmap image) throws NotFoundException {
    return decodeMultiple(image, null);
  }

  @Override
  public Result[] decodeMultiple(BinaryBitmap image, Map<DecodeHintType,?> hints) throws NotFoundException {
    List<Result> results = new ArrayList<>();
    DetectorResult[] detectorResults = new MultiDetector(image.getBlackMatrix()).detectMulti(hints);
    for (DetectorResult detectorResult : detectorResults) {
      try {
        DecoderResult decoderResult = getDecoder().decode(detectorResult.getBits(), hints);
        ResultPoint[] points = detectorResult.getPoints();
        // If the code was mirrored: swap the bottom-left and the top-right points.
        if (decoderResult.getOther() instanceof QRCodeDecoderMetaData) {
          ((QRCodeDecoderMetaData) decoderResult.getOther()).applyMirroredCorrection(points);
        }
        Result result = new Result(decoderResult.getText(), decoderResult.getRawBytes(), points,
                                   BarcodeFormat.QR_CODE);
        List<byte[]> byteSegments = decoderResult.getByteSegments();
        if (byteSegments != null) {
          result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegments);
        }
        String ecLevel = decoderResult.getECLevel();
        if (ecLevel != null) {
          result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, ecLevel);
        }
        if (decoderResult.hasStructuredAppend()) {
          result.putMetadata(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE,
                             decoderResult.getStructuredAppendSequenceNumber());
          result.putMetadata(ResultMetadataType.STRUCTURED_APPEND_PARITY,
                             decoderResult.getStructuredAppendParity());
        }
        results.add(result);
      } catch (ReaderException re) {
        // ignore and continue 
      }
    }
    if (results.isEmpty()) {
      return EMPTY_RESULT_ARRAY;
    } else {
      results = processStructuredAppend(results);
      return results.toArray(EMPTY_RESULT_ARRAY);
    }
  }

  private static List<Result> processStructuredAppend(List<Result> results) {
    boolean hasSA = false;

    // first, check, if there is at least on SA result in the list
    for (Result result : results) {
      if (result.getResultMetadata().containsKey(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE)) {
        hasSA = true;
        break;
      }
    }
    if (!hasSA) {
      return results;
    }

    // it is, second, split the lists and built a new result list
    List<Result> newResults = new ArrayList<>();
    List<Result> saResults = new ArrayList<>();
    for (Result result : results) {
      newResults.add(result);
      if (result.getResultMetadata().containsKey(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE)) {
        saResults.add(result);
      }
    }
    // sort and concatenate the SA list items
    Collections.sort(saResults, new SAComparator());
    StringBuilder concatedText = new StringBuilder();
    int rawBytesLen = 0;
    int byteSegmentLength = 0;
    for (Result saResult : saResults) {
      concatedText.append(saResult.getText());
      rawBytesLen += saResult.getRawBytes().length;
      if (saResult.getResultMetadata().containsKey(ResultMetadataType.BYTE_SEGMENTS)) {
        @SuppressWarnings("unchecked")
        Iterable<byte[]> byteSegments =
            (Iterable<byte[]>) saResult.getResultMetadata().get(ResultMetadataType.BYTE_SEGMENTS);
        for (byte[] segment : byteSegments) {
          byteSegmentLength += segment.length;
        }
      }
    }
    byte[] newRawBytes = new byte[rawBytesLen];
    byte[] newByteSegment = new byte[byteSegmentLength];
    int newRawBytesIndex = 0;
    int byteSegmentIndex = 0;
    for (Result saResult : saResults) {
      System.arraycopy(saResult.getRawBytes(), 0, newRawBytes, newRawBytesIndex, saResult.getRawBytes().length);
      newRawBytesIndex += saResult.getRawBytes().length;
      if (saResult.getResultMetadata().containsKey(ResultMetadataType.BYTE_SEGMENTS)) {
        @SuppressWarnings("unchecked")
        Iterable<byte[]> byteSegments =
            (Iterable<byte[]>) saResult.getResultMetadata().get(ResultMetadataType.BYTE_SEGMENTS);
        for (byte[] segment : byteSegments) {
          System.arraycopy(segment, 0, newByteSegment, byteSegmentIndex, segment.length);
          byteSegmentIndex += segment.length;
        }
      }
    }
    Result newResult = new Result(concatedText.toString(), newRawBytes, NO_POINTS, BarcodeFormat.QR_CODE);
    if (byteSegmentLength > 0) {
      Collection<byte[]> byteSegmentList = new ArrayList<>();
      byteSegmentList.add(newByteSegment);
      newResult.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegmentList);
    }
    newResults.add(newResult);
    return newResults;
  }

  private static final class SAComparator implements Comparator<Result>, Serializable {
    @Override
    public int compare(Result a, Result b) {
      int aNumber = (int) a.getResultMetadata().get(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE);
      int bNumber = (int) b.getResultMetadata().get(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE);
      return Integer.compare(aNumber, bNumber);
    }
  }

}
