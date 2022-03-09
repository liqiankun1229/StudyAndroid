package com.lqk.net

import java.io.IOException
import java.io.InputStream
import java.security.*
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * @author LQK
 * @time 2019/3/11 16:32
 * @remark
 */
object NetUtil {
    fun getSslSocketFactory(certificates: Array<InputStream>, bksFile: InputStream, password: String): SSLSocketFactory {
        try {
            val trustManagers = prepareTrustManager(*certificates)
            val keyManagers = prepareKeyManager(bksFile, password)
            val sslContext = SSLContext.getInstance("SSL")

            sslContext.init(keyManagers, arrayOf<TrustManager>(MyTrustManager(chooseTrustManager(trustManagers!!)!!)), SecureRandom())
            return sslContext.socketFactory
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError(e)
        } catch (e: KeyManagementException) {
            throw AssertionError(e)
        } catch (e: KeyStoreException) {
            throw AssertionError(e)
        }

    }

    private fun prepareTrustManager(vararg certificates: InputStream): Array<TrustManager>? {
        if (certificates == null || certificates.isEmpty())
            return null
        try {

            val certificateFactory = CertificateFactory.getInstance("X.509")
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null)
            for ((index, certificate) in certificates.withIndex()) {
                val certificateAlias = Integer.toString(index)
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate))
                try {
                    certificate.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            var trustManagerFactory: TrustManagerFactory? = null

            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory!!.init(keyStore)

            return trustManagerFactory.trustManagers
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null

    }

    private fun prepareKeyManager(bksFile: InputStream?, password: String?): Array<KeyManager>? {
        try {
            if (bksFile == null || password == null)
                return null

            val clientKeyStore = KeyStore.getInstance("BKS")
            clientKeyStore.load(bksFile, password.toCharArray())
            val keyManagerFactory = KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm())
            keyManagerFactory.init(clientKeyStore, password.toCharArray())
            return keyManagerFactory.keyManagers

        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnrecoverableKeyException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private fun chooseTrustManager(trustManagers: Array<TrustManager>): X509TrustManager? {
        for (trustManager in trustManagers) {
            if (trustManager is X509TrustManager) {
                return trustManager as X509TrustManager
            }
        }
        return null
    }

    private class MyTrustManager : X509TrustManager {
        var defaultTrustManager: X509TrustManager? = null
        var localTrustManager: X509TrustManager? = null

        constructor(localTrustManager: X509TrustManager) : super() {
            val var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            var4.init(null as KeyStore?)
            defaultTrustManager = chooseTrustManager(var4.trustManagers)
            this.localTrustManager = localTrustManager
        }

        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            try {
                defaultTrustManager!!.checkServerTrusted(chain, authType)
            } catch (ce: CertificateException) {
                localTrustManager?.checkServerTrusted(chain, authType)
            }
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }


    fun initSSLSocketFactory(): SSLSocketFactory {
        var sslContext: SSLContext? = null
        try {
            sslContext = SSLContext.getInstance("SSL")
            val xTrustArray = arrayOf(initTrustManager())
            sslContext!!.init(null,
                    xTrustArray, SecureRandom())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return sslContext!!.getSocketFactory()
    }

    fun initTrustManager(): X509TrustManager {
        return object : X509TrustManager {

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }
        }
    }
}