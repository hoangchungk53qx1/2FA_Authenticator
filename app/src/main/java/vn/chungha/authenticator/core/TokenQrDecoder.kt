package vn.chungha.authenticator.core

import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenQRCodeDecoder @Inject constructor(private val CodeReader: QRCodeReader) {

    private val tag: String = TokenQRCodeDecoder::class.java.simpleName

    private lateinit var imageData: ByteArray

    fun parseQRCode(image: ImageProxy): String? {

        // In some phones, row stride is larger than the width. Use row stride instead to avoid
        // buffer overflow
        val rowStride = image.planes[0].rowStride

        if (!::imageData.isInitialized) {
            imageData = ByteArray(rowStride * image.height)
        }

        synchronized(imageData) {
            // Only Y component of YUV is needed

            val y = image.planes[0]
            val ySize = y.buffer.remaining()

            if (ySize > imageData.size) {
                imageData = ByteArray(ySize)
            }

            y.buffer.get(imageData, 0, ySize)

            val ls = PlanarYUVLuminanceSource(
                imageData, rowStride, image.height,
                0, 0, rowStride, image.height, false)

            return try {
                CodeReader.decode(BinaryBitmap(HybridBinarizer(ls))).text
            } catch (e: NotFoundException) {
                e.printStackTrace()
                null
            } catch (e: ChecksumException) {
                e.printStackTrace()
                null
            } catch (e: FormatException) {
                null
            } finally {
                CodeReader.reset()
            }
        }
    }
}