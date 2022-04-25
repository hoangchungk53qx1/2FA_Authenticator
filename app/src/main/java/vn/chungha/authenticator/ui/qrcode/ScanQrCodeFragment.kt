package vn.chungha.authenticator.ui.qrcode

import android.Manifest
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.Surface
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import vn.chungha.authenticator.base.BaseFragment
import vn.chungha.authenticator.core.OtpTokenFactory
import vn.chungha.authenticator.core.TokenQRCodeDecoder
import vn.chungha.authenticator.databinding.FragmentQrCodeBinding
import vn.chungha.authenticator.ui.MainActivity
import java.util.concurrent.ExecutorService
import javax.inject.Inject

@AndroidEntryPoint
class ScanQrCodeFragment() : BaseFragment<FragmentQrCodeBinding, MainActivity>() {

    @Inject
    lateinit var executorService: ExecutorService

    @Inject
    lateinit var tokenQRCodeDecoder: TokenQRCodeDecoder

    private var foundToken = false


    override fun onInflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentQrCodeBinding = FragmentQrCodeBinding.inflate(inflater)

    override fun setupView() {
        PermissionX.init(activity)
            .permissions(Manifest.permission.CAMERA)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "Core fundamental are based on these permissions",
                    "OK",
                    "Cancel"
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "You need to allow necessary permissions in Settings manually",
                    "OK",
                    "Cancel"
                )
            }
            .request { allGranted, _, deniedList ->
                if (allGranted) {
                    startCamera()
                    Toast.makeText(
                        requireContext(),
                        "All permissions are granted",
                        Toast.LENGTH_LONG
                    ).show()

                } else {
                    Toast.makeText(
                        requireContext(),
                        "These permissions are denied: $deniedList",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    override fun setupData() {
//        showBottomBar(false)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({

            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

            val preview = Preview.Builder().apply {
                setTargetAspectRatio(AspectRatio.RATIO_16_9)
                setTargetRotation(Surface.ROTATION_0)
            }.build()

            preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)

            val imageAnalysis = ImageAnalysis.Builder().apply {
                setBackgroundExecutor(executorService)
                setTargetAspectRatio(AspectRatio.RATIO_16_9)
                setTargetRotation(Surface.ROTATION_0)
            }.build()

            imageAnalysis.setAnalyzer(executorService) { imageProxy: ImageProxy ->
                analyzeImage(imageProxy)
            }

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private fun analyzeImage(imageProxy: ImageProxy) {
        if (foundToken) {
            return
        }

        val tokenString = imageProxy.use { image ->
            tokenQRCodeDecoder.parseQRCode(image) ?: return
        }

        foundToken = true

//        Toast.makeText(requireContext(),"Token String ... $tokenString ",Toast.LENGTH_SHORT)
        Timber.d("Token@@@### >>> $tokenString")
        lifecycleScope.launch {
            val token = try {
                val t = OtpTokenFactory.createFromUri((Uri.parse(tokenString)))
//                otpTokenDatabase.otpTokenDao().insert(t)
                t
            } catch (e: Throwable) {
                popBackStack()
                return@launch
            }

//            Toast.makeText(this@ScanTokenActivity, R.string.add_token_success, Toast.LENGTH_SHORT).show()

//            setResult(AppCompatActivity.RESULT_OK)
            if (token.imagePath == null) {
                popBackStack()
                return@launch
            }

            Glide.with(requireContext())
                .load(token.imagePath)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.imageView.alpha = 0.9f
                        binding.imageView.postDelayed({
//                            finish()
                            popBackStack()
                        }, 2000)
                        return false
                    }

                })
                .into(binding.imageView)
        }
    }
}