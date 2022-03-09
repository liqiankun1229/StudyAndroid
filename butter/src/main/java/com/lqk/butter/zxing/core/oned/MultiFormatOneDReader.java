/*
 * Copyright 2008 ZXing authors
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

package com.lqk.butter.zxing.core.oned;

import com.lqk.butter.zxing.core.BarcodeFormat;
import com.lqk.butter.zxing.core.DecodeHintType;
import com.lqk.butter.zxing.core.NotFoundException;
import com.lqk.butter.zxing.core.Reader;
import com.lqk.butter.zxing.core.ReaderException;
import com.lqk.butter.zxing.core.Result;
import com.lqk.butter.zxing.core.common.BitArray;
import com.lqk.butter.zxing.core.oned.rss.RSS14Reader;
import com.lqk.butter.zxing.core.oned.rss.expanded.RSSExpandedReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class MultiFormatOneDReader extends com.lqk.butter.zxing.core.oned.OneDReader {

  private static final com.lqk.butter.zxing.core.oned.OneDReader[] EMPTY_ONED_ARRAY = new com.lqk.butter.zxing.core.oned.OneDReader[0];

  private final com.lqk.butter.zxing.core.oned.OneDReader[] readers;

  public MultiFormatOneDReader(Map<DecodeHintType,?> hints) {
    @SuppressWarnings("unchecked")
    Collection<BarcodeFormat> possibleFormats = hints == null ? null :
        (Collection<BarcodeFormat>) hints.get(DecodeHintType.POSSIBLE_FORMATS);
    boolean useCode39CheckDigit = hints != null &&
        hints.get(DecodeHintType.ASSUME_CODE_39_CHECK_DIGIT) != null;
    Collection<com.lqk.butter.zxing.core.oned.OneDReader> readers = new ArrayList<>();
    if (possibleFormats != null) {
      if (possibleFormats.contains(BarcodeFormat.EAN_13) ||
          possibleFormats.contains(BarcodeFormat.UPC_A) ||
          possibleFormats.contains(BarcodeFormat.EAN_8) ||
          possibleFormats.contains(BarcodeFormat.UPC_E)) {
        readers.add(new MultiFormatUPCEANReader(hints));
      }
      if (possibleFormats.contains(BarcodeFormat.CODE_39)) {
        readers.add(new com.lqk.butter.zxing.core.oned.Code39Reader(useCode39CheckDigit));
      }
      if (possibleFormats.contains(BarcodeFormat.CODE_93)) {
        readers.add(new com.lqk.butter.zxing.core.oned.Code93Reader());
      }
      if (possibleFormats.contains(BarcodeFormat.CODE_128)) {
        readers.add(new com.lqk.butter.zxing.core.oned.Code128Reader());
      }
      if (possibleFormats.contains(BarcodeFormat.ITF)) {
         readers.add(new com.lqk.butter.zxing.core.oned.ITFReader());
      }
      if (possibleFormats.contains(BarcodeFormat.CODABAR)) {
         readers.add(new com.lqk.butter.zxing.core.oned.CodaBarReader());
      }
      if (possibleFormats.contains(BarcodeFormat.RSS_14)) {
         readers.add(new RSS14Reader());
      }
      if (possibleFormats.contains(BarcodeFormat.RSS_EXPANDED)) {
        readers.add(new RSSExpandedReader());
      }
    }
    if (readers.isEmpty()) {
      readers.add(new MultiFormatUPCEANReader(hints));
      readers.add(new Code39Reader());
      readers.add(new CodaBarReader());
      readers.add(new Code93Reader());
      readers.add(new Code128Reader());
      readers.add(new ITFReader());
      readers.add(new RSS14Reader());
      readers.add(new RSSExpandedReader());
    }
    this.readers = readers.toArray(EMPTY_ONED_ARRAY);
  }

  @Override
  public Result decodeRow(int rowNumber,
                          BitArray row,
                          Map<DecodeHintType,?> hints) throws NotFoundException {
    for (OneDReader reader : readers) {
      try {
        return reader.decodeRow(rowNumber, row, hints);
      } catch (ReaderException re) {
        // continue
      }
    }

    throw NotFoundException.getNotFoundInstance();
  }

  @Override
  public void reset() {
    for (Reader reader : readers) {
      reader.reset();
    }
  }

}
