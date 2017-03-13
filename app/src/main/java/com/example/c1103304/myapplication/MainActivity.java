package com.example.c1103304.myapplication;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.annotation.NonNull;


import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Fragment  implements View.OnClickListener{

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final String FRAGMENT_DIALOG = "dialog";
    float screen_x,screen_y,center;
    private TextView txtlabel;
    public String fileName="";
    public String fileDir="";
    private int[] mRes = {R.id.info,R.id.button2,R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8};
    private int[] ACbtn= {R.id.ac01,R.id.ac02,R.id.ac03,R.id.ac04,R.id.ac05,R.id.ac06,R.id.ac07};
    private int[][] mACbtn= {{R.id.ac011,R.id.ac012},{R.id.ac021,R.id.ac022},{R.id.ac031,R.id.ac032},{R.id.ac041,R.id.ac042}
            ,{R.id.ac051,R.id.ac052},{R.id.ac061,R.id.ac062},{R.id.ac071,R.id.ac072}};
    private int[] Sibtn= {R.id.ac01,R.id.si02,R.id.si03,R.id.ac06,R.id.ac07};
    private int[][] mSibtn= {{R.id.ac011,R.id.ac012},{R.id.si021,R.id.si022},{R.id.si031,R.id.si032,R.id.ac05},
            {R.id.ac061,R.id.ac062},{R.id.ac071,R.id.ac072},{R.id.ac041,R.id.ac022},{R.id.ac051,R.id.ac052}};
    private int[] Finbtn= {R.id.fin01,R.id.fin02,R.id.fin03,R.id.fin04};
    private int[] hubtn= {R.id.ac01,R.id.ac02,R.id.ac03,R.id.hu04,R.id.ac06,R.id.ac07};
    private int[][] mhubtn= {{R.id.ac011,R.id.ac012},{R.id.ac021,R.id.ac022},{R.id.ac031,R.id.ac032},{R.id.ac041,R.id.ac022},{R.id.ac061,R.id.ac062},{R.id.ac071,R.id.ac072}};
    private int[] Bsbtn= {R.id.ac01,R.id.bs02,R.id.bs03,R.id.bs04,R.id.ac06,R.id.ac07};
    private int[][] mBsbtn= {{R.id.ac011,R.id.ac012},{R.id.bs021,R.id.bs022},{R.id.ac061,R.id.ac062},{R.id.ac071,R.id.ac072}};
    private int[] Bdbtn= {R.id.ac01,R.id.bd02,R.id.bd03,R.id.ac05,R.id.ac06,R.id.ac07};
    private int[][] mBdbtn= {{R.id.ac011,R.id.ac012},{R.id.ac021,R.id.ac022},{R.id.bs021,R.id.bs022},{R.id.ac051,R.id.ac052},{R.id.ac061,R.id.ac062},{R.id.ac071,R.id.ac072}};
    private int[] Fixbtn= {R.id.fix01,R.id.fix02,R.id.fix03,R.id.fix04,R.id.fix05,R.id.fix06};
    private String firstStr="-",secStr="-",thrStr="-",totalstr="";
    private List<Button> mButton;
    private int clayer = 0,sslayer = 0;
    private String piclabel="";
    private boolean isOpen;
    private Button take;
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private static final String TAG = "Camera2BasicFragment";

    /**
     * Camera state: Showing camera preview.
     */
    private static final int STATE_PREVIEW = 0;

    /**
     * Camera state: Waiting for the focus to be locked.
     */
    private static final int STATE_WAITING_LOCK = 1;

    /**
     * Camera state: Waiting for the exposure to be precapture state.
     */
    private static final int STATE_WAITING_PRECAPTURE = 2;

    /**
     * Camera state: Waiting for the exposure state to be something other than precapture.
     */
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;

    /**
     * Camera state: Picture was taken.
     */
    private static final int STATE_PICTURE_TAKEN = 4;

    /**
     * Max preview width that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_WIDTH = 1920;

    /**
     * Max preview height that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_HEIGHT = 1080;

    /**
     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a
     * {@link TextureView}.
     */
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }

    };

    /**
     * ID of the current {@link CameraDevice}.
     */
    private String mCameraId;

    /**
     * An {@link AutoFitTextureView} for camera preview.
     */
    private AutoFitTextureView mTextureView;

    /**
     * A {@link CameraCaptureSession } for camera preview.
     */
    private CameraCaptureSession mCaptureSession;

    /**
     * A reference to the opened {@link CameraDevice}.
     */
    private CameraDevice mCameraDevice;

    /**
     * The {@link android.util.Size} of camera preview.
     */
    private Size mPreviewSize;
    /**
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its state.
     */

    final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            Activity activity = getActivity();
            if (null != activity) {
                activity.finish();
            }
        }

    };
    private HandlerThread mBackgroundThread;

    /**
     * A {@link Handler} for running tasks in the background.
     */
    private Handler mBackgroundHandler;

    /**
     * An {@link ImageReader} that handles still image capture.
     */
    private ImageReader mImageReader;

    /**
     * This is the output file for our picture.
     */
    private File mFile;

    /**
     * This a callback object for the {@link ImageReader}. "onImageAvailable" will be called when a
     * still image is ready to be saved.
     */
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
            Log.d("MYLOG","新增照片成功");
            mFile = new File(fileDir,fileName);
            mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), mFile));
        }

    };

    /**
     * {@link CaptureRequest.Builder} for the camera preview
     */
    private CaptureRequest.Builder mPreviewRequestBuilder;

    /**
     * {@link CaptureRequest} generated by {@link #mPreviewRequestBuilder}
     */
    private CaptureRequest mPreviewRequest;

    /**
     * The current state of camera state for taking pictures.
     *
     * @see #mCaptureCallback
     */
    private int mState = STATE_PREVIEW;

    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    /**
     * Whether the current camera device supports Flash or not.
     */
    private boolean mFlashSupported;

    /**
     * Orientation of the camera sensor
     */
    private int mSensorOrientation;

    /**
     * A {@link CameraCaptureSession.CaptureCallback} that handles events related to JPEG capture.
     */
    CameraCaptureSession.CaptureCallback mCaptureCallback
            = new CameraCaptureSession.CaptureCallback() {

        void process(CaptureResult result) {
            switch (mState) {
                case STATE_PREVIEW: {
                    // We have nothing to do when the camera preview is working normally.
                    break;
                }
                case STATE_WAITING_LOCK: {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    /**
                     * 判断可以立即拍摄的autoFocusState增加到4种.
                     */
                    if (afState == null) {
                        captureStillPicture();
                    } else if (CaptureResult.CONTROL_AF_STATE_PASSIVE_FOCUSED == afState ||
                            CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                            CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        // CONTROL_AE_STATE can be null on some devices
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        /**
                         * 判断可以立即拍摄的autoExposureState增加到4种.
                         */
                        if (aeState == null ||
                                aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED ||
                                aeState == CaptureResult.CONTROL_AE_STATE_LOCKED ||
                                aeState == CaptureResult.CONTROL_AE_STATE_FLASH_REQUIRED) {
                            mState = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            runPrecaptureSequence();
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
            }
        }
        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            process(result);
        }

    };
    /**
     * Shows a {@link Toast} on the UI thread.
     *
     * @param text The message to show
     */
    private void showToast(final String text) {
        final Activity activity = getActivity();
        //Log.d("MYLOG",getActivity().toString());
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        double minRatio = ((double) w) / ((double) h) * 0.95;
        double maxRatio = ((double) w) / ((double) h) * 1.05;
        for (Size option : choices) {
            double ratio = ((double) option.getWidth()) / ((double) option.getHeight());
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    /**
                     * 现在允许宽高比相对于16:9有正负5%的误差.
                     */
                    ratio >= minRatio && ratio <= maxRatio) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }
        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }
    public static MainActivity newInstance() {
        return new MainActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        txtlabel = (TextView)view.findViewById(R.id.label);
        txtlabel.setText("請點選拍照標籤");
        take = (Button) view.findViewById(R.id.picture);
        take.setOnClickListener(this);
        //view.findViewById(R.id.info).setOnClickListener(this);
        scaleHeight();
        mTextureView = (AutoFitTextureView) view.findViewById(R.id.texture);

        isOpen = false;
        mButton = new ArrayList<>();
        /* mRes mButton : 0 ~ 7 */
        for (int i = 0; i < mRes.length; i++) {
           Button button = (Button) view.findViewById(mRes[i]);
            button.setOnClickListener(this);
            mButton.add(button);
        }
        /* AC01~07    mButton : 8 ~ 14 */
        for (int i = 0; i < ACbtn.length; i++) {
            Button button = (Button) view.findViewById(ACbtn[i]);
            button.setEnabled(false);
            button.setOnClickListener(this);
            mButton.add(button);
        }
        /* mButton : 15 ~ 28 */
        for (int i = 0; i < mACbtn.length; i++) {
            for(int j = 0; j < mACbtn[i].length;j++) {
                Button button = (Button) view.findViewById(mACbtn[i][j]);
                button.setEnabled(false);
                button.setOnClickListener(this);
                mButton.add(button);
            }
        }

         /* Si01~05    mButton : 29 ~ 33 */
        for (int i = 0; i < Sibtn.length; i++) {
            Button button = (Button) view.findViewById(Sibtn[i]);
            button.setEnabled(false);
            button.setOnClickListener(this);
            mButton.add(button);
        }
        /*  mButton : 34 ~ 48 */
        for (int i = 0; i < mSibtn.length; i++) {
            for(int j = 0; j < mSibtn[i].length;j++) {
                Button button = (Button) view.findViewById(mSibtn[i][j]);
                button.setEnabled(false);
                button.setOnClickListener(this);
                mButton.add(button);
            }
        }

         /* Fin01~05    mButton : 49 ~ 52 */
        for (int i = 0; i < Finbtn.length; i++) {
            Button button = (Button) view.findViewById(Finbtn[i]);
            button.setEnabled(false);
            button.setOnClickListener(this);
            mButton.add(button);
        }

        /* hu01~06    mButton : 53 ~ 58 */
        for (int i = 0; i < hubtn.length; i++) {
            Button button = (Button) view.findViewById(hubtn[i]);
            button.setEnabled(false);
            button.setOnClickListener(this);
            mButton.add(button);
        }
        /*  mButton : 59 ~ 70 */
        for (int i = 0; i < mhubtn.length; i++) {
            for(int j = 0; j < mhubtn[i].length;j++) {
                Button button = (Button) view.findViewById(mhubtn[i][j]);
                button.setEnabled(false);
                button.setOnClickListener(this);
                mButton.add(button);
            }
        }

        /* Bs01~06    mButton : 71 ~ 76 */
        for (int i = 0; i < Bsbtn.length; i++) {
            Button button = (Button) view.findViewById(Bsbtn[i]);
            button.setEnabled(false);
            button.setOnClickListener(this);
            mButton.add(button);
        }
        /*  mButton : 77 ~ 84 */
        for (int i = 0; i < mBsbtn.length; i++) {
            for(int j = 0; j < mBsbtn[i].length;j++) {
                Button button = (Button) view.findViewById(mBsbtn[i][j]);
                button.setEnabled(false);
                button.setOnClickListener(this);
                mButton.add(button);
            }
        }

        /* Bd01~06    mButton : 85 ~ 90 */
        for (int i = 0; i < Bdbtn.length; i++) {
            Button button = (Button) view.findViewById(Bdbtn[i]);
            button.setEnabled(false);
            button.setOnClickListener(this);
            mButton.add(button);
        }
        /*  mButton : 91 ~ 102 */
        for (int i = 0; i < mBdbtn.length; i++) {
            for(int j = 0; j < mBdbtn[i].length;j++) {
                Button button = (Button) view.findViewById(mBdbtn[i][j]);
                button.setEnabled(false);
                button.setOnClickListener(this);
                mButton.add(button);
            }
        }

        /* Fix01~06    103 ~ 108 */
        for (int i = 0; i < Fixbtn.length; i++) {
            Button button = (Button) view.findViewById(Fixbtn[i]);
            button.setEnabled(false);
            button.setOnClickListener(this);
            mButton.add(button);
        }


        //mButton.get(0).setEnabled(false);
        openMenu();
    }

    /*
    *  相片檔案路徑  /storage/emulated/0/DCIM
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"";

        mFile = new File(fileDir, fileName);
        Log.d("MYLOG",fileDir);
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();

        // When the screen is turned off and turned back on, the SurfaceTexture is already
        // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
        // a camera and start preview from here (otherwise, we wait until the surface is ready in
        // the SurfaceTextureListener).
        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }
    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }



    private void setUpCameraOutputs(int width, int height) {
        Activity activity = getActivity();
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics
                        = manager.getCameraCharacteristics(cameraId);

                // We don't use a front facing camera in this sample.
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                StreamConfigurationMap map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                // For still image captures, we use the largest available size.
                Size largest = Collections.max(
                        Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                        new CompareSizesByArea());
                mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                        ImageFormat.JPEG, /*maxImages*/2);
                mImageReader.setOnImageAvailableListener(
                        mOnImageAvailableListener, mBackgroundHandler);

                // Find out if we need to swap dimension to get the preview size relative to sensor
                // coordinate.
                int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
                //noinspection ConstantConditions
                mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                boolean swappedDimensions = false;
                switch (displayRotation) {
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                            swappedDimensions = true;
                        }
                        break;
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                            swappedDimensions = true;
                        }
                        break;
                    default:
                        Log.e(TAG, "Display rotation is invalid: " + displayRotation);
                }

                Point displaySize = new Point();
                activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
                int rotatedPreviewWidth = width;
                int rotatedPreviewHeight = height;
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;

                if (swappedDimensions) {
                    rotatedPreviewWidth = height;
                    rotatedPreviewHeight = width;
                    maxPreviewWidth = displaySize.y;
                    maxPreviewHeight = displaySize.x;
                }

                if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH;
                }

                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }

                // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
                // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
                // garbage capture data.
                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                        maxPreviewHeight, largest);

                // We fit the aspect ratio of TextureView to the size of preview we picked.
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mTextureView.setAspectRatio(
                            mPreviewSize.getWidth(), mPreviewSize.getHeight());
                } else {
                    mTextureView.setAspectRatio(
                            mPreviewSize.getHeight(), mPreviewSize.getWidth());
                }

                // Check if the flash is supported.
                Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                mFlashSupported = available == null ? false : available;

                mCameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
            ErrorDialog.newInstance("can not support!")
                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
        }
    }



    private void openCamera(int width, int height) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                // 權限確認
            return;
        }
        setUpCameraOutputs(width, height);
        configureTransform(width, height);
        Activity activity = getActivity();
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    /**
     * Closes the current {@link CameraDevice}.
     */
    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCaptureSession) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    /**
     * Starts a background thread and its {@link Handler}.
     */
    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new {@link CameraCaptureSession} for camera preview.
     */
    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);

            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder
                    = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            // Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == mCameraDevice) {
                                return;
                            }

                            // When the session is ready, we start displaying the preview.
                            mCaptureSession = cameraCaptureSession;
                            try {
                                // Auto focus should be continuous for camera preview.
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                // Flash is automatically enabled when necessary.
                                setAutoFlash(mPreviewRequestBuilder);

                                // Finally, we start displaying the camera preview.
                                mPreviewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(mPreviewRequest,
                                        mCaptureCallback, mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                            showToast("Failed");
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configures the necessary {@link android.graphics.Matrix} transformation to `mTextureView`.
     * This method should be called after the camera preview size is determined in
     * setUpCameraOutputs and also the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = getActivity();
        if (null == mTextureView || null == mPreviewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    /**
     * Initiate a still image capture.
     */
    private void takePicture() {
        //take.setEnabled(false);
        if(thrStr.length()>1){
            Calendar c = Calendar.getInstance();
            String timename = c.getTimeInMillis()+"";
            fileName = timename+".jpg";
            photodata p1 = new photodata();
            p1.setSerial("01");
            p1.setTotalInfo(totalstr);
            p1.setFilename(fileName);
            p1.setHead_name(firstStr);
            p1.setSub_name(secStr);
            p1.setLast_name(thrStr);
            p1.setIsupload(false);
            welcomePage.insertdata(p1);
            showToast("正在調整焦距...請勿晃動..");
            lockFocus();
        }else{
            showToast("請先選擇拍照項目");
        }
    }

    /**
     * Lock the focus as the first step for a still image capture.
     */
    private void lockFocus() {
        try {
            // This is how to tell the camera to lock focus.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the lock.
            mState = STATE_WAITING_LOCK;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run the precapture sequence for capturing a still image. This method should be called when
     * we get a response in {@link #mCaptureCallback} from {@link #lockFocus()}.
     */
    private void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            mState = STATE_WAITING_PRECAPTURE;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Capture a still picture. This method should be called when we get a response in
     * {@link #mCaptureCallback} from both {@link #lockFocus()}.
     */
    private void captureStillPicture() {
        try {
            final Activity activity = getActivity();
            if (null == activity || null == mCameraDevice) {
                return;
            }
            // This is the CaptureRequest.Builder that we use to take a picture.
            final CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());

            // Use the same AE and AF modes as the preview.
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            setAutoFlash(captureBuilder);

            // Orientation
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            CameraCaptureSession.CaptureCallback CaptureCallback
                    = new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {
                    showToast("Saved: " + mFile);
                    Log.d("MYLOG", mFile.toString());
                    unlockFocus();
                }
            };

            mCaptureSession.stopRepeating();
            mCaptureSession.capture(captureBuilder.build(), CaptureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the JPEG orientation from the specified screen rotation.
     *
     * @param rotation The screen rotation.
     * @return The JPEG orientation (one of 0, 90, 270, and 360)
     */
    private int getOrientation(int rotation) {
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    /**
     * Unlock the focus. This method should be called when still image capture sequence is
     * finished.
     */
    private void unlockFocus() {
        try {
            // Reset the auto-focus trigger
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            setAutoFlash(mPreviewRequestBuilder);
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
            // After this, the camera will go back to the normal state of preview.
            mState = STATE_PREVIEW;
            mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.picture:

                takePicture();
                //Log.d("MYLOG","點擊拍照");
                break;

            case R.id.info:
                if(thrStr.length()>1){
                    if (isOpen) {
                        closeMenu();
                        isOpen = false;
                    } else {
                        openMenu();
                        isOpen = true;
                    }
                }else{
                    showToast("請先選擇拍照項目");
                }
                break;
            case R.id.button2:
                firstStr = view.getTag().toString();
                closeMenu();
                clayer = 1;
                sslayer = 0;
                openMenu();
                isOpen = true;
                break;
            case R.id.button3:
                firstStr = view.getTag().toString();
                closeMenu();
                clayer = 1;
                sslayer = 1;
                openMenu();
                isOpen = true;
                break;
            case R.id.button4:
                firstStr = view.getTag().toString();
                closeMenu();
                clayer = 1;
                sslayer = 2;
                openMenu();
                isOpen = true;
                break;
            case R.id.button5:
                firstStr = view.getTag().toString();
                closeMenu();
                clayer = 1;
                sslayer = 3;
                openMenu();
                isOpen = true;
                break;
            case R.id.button6:
                firstStr = view.getTag().toString();
                closeMenu();
                clayer = 1;
                sslayer = 4;
                openMenu();
                isOpen = true;
                break;
            case R.id.button7:
                firstStr = view.getTag().toString();
                closeMenu();
                clayer = 1;
                sslayer = 5;
                openMenu();
                isOpen = true;
                break;
            case R.id.button8:
                firstStr = view.getTag().toString();
                closeMenu();
                clayer = 1;
                sslayer = 6;
                openMenu();
                isOpen = true;
                break;
            case R.id.ac01:
                secStr = view.getTag().toString();
                closeMenu();
                isOpen = false;
                btn_animation(mButton.get(15),mButton.get(16));
                break;
            case R.id.ac02:
                secStr = view.getTag().toString();
                closeMenu();
                isOpen = false;
                btn_animation(mButton.get(17),mButton.get(18));
                break;
            case R.id.ac03:
                secStr = view.getTag().toString();
                closeMenu();
                isOpen = false;
                btn_animation(mButton.get(19),mButton.get(20));
                break;
            case R.id.ac04:
                secStr = view.getTag().toString();
                closeMenu();
                isOpen = false;
                btn_animation(mButton.get(21),mButton.get(22));
                break;
            case R.id.ac05:
                secStr = view.getTag().toString();
                switch (sslayer){
                    default:
                        closeMenu();
                        isOpen = false;
                        break;
                    case 1:
                        btn_animationClose(mButton.get(38),mButton.get(39),mButton.get(12));
                        break;
                }

                //
                btn_animation(mButton.get(23),mButton.get(24));
                break;
            case R.id.ac06:
                secStr = view.getTag().toString();
                closeMenu();
                isOpen = false;
                btn_animation(mButton.get(25),mButton.get(26));
                break;
            case R.id.ac07:
                secStr = view.getTag().toString();
                closeMenu();
                isOpen = false;
                btn_animation(mButton.get(27),mButton.get(28));
                break;
            case R.id.ac011:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(15),mButton.get(16));
                break;
            case R.id.ac012:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(15),mButton.get(16));
                break;
            case R.id.ac021:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(17),mButton.get(18));
                break;
            case R.id.ac022:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(21),mButton.get(17),mButton.get(18));
                break;
            case R.id.ac031:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(19),mButton.get(20));
                break;
            case R.id.ac032:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(19),mButton.get(20));
                break;
            case R.id.ac041:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(21),mButton.get(22),mButton.get(18));
                break;
            case R.id.ac042:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(21),mButton.get(22));
                break;
            case R.id.ac051:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(23),mButton.get(24));
                break;
            case R.id.ac052:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(23),mButton.get(24));
                break;
            case R.id.ac061:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(25),mButton.get(26));
                break;
            case R.id.ac062:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(25),mButton.get(26));
                break;
            case R.id.ac071:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(27),mButton.get(28));
                break;
            case R.id.ac072:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(27),mButton.get(28));
                break;
            case R.id.si02:
                secStr =  view.getTag().toString();
                closeMenu();
                isOpen = false;
                btn_animation(mButton.get(36),mButton.get(37));
                break;
            case R.id.si021:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(36),mButton.get(37));
                break;
            case R.id.si022:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(36),mButton.get(37));
                break;
            case R.id.si03:
                secStr =  view.getTag().toString();
                closeMenu();
                isOpen = false;
                btn_animation(mButton.get(38),mButton.get(39),mButton.get(12));
                break;
            case R.id.si031:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(38),mButton.get(39),mButton.get(12));
                btn_animation(mButton.get(21),mButton.get(18));
                break;
            case R.id.si032:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(38),mButton.get(39),mButton.get(12));
                break;
            case R.id.fin01:
                secStr =  view.getTag().toString();
                thrStr = "   ";
                closeMenu();
                isOpen = false;
                break;
            case R.id.fin02:
                secStr =  view.getTag().toString();
                thrStr = "   ";
                closeMenu();
                isOpen = false;
                break;
            case R.id.fin03:
                secStr =  view.getTag().toString();
                thrStr = "   ";
                closeMenu();
                isOpen = false;
                break;
            case R.id.fin04:
                secStr =  view.getTag().toString();
                thrStr = "   ";
                closeMenu();
                isOpen = false;
                break;
            case R.id.hu04:
                secStr =  view.getTag().toString();
                closeMenu();
                isOpen = false;
                btn_animation(mButton.get(21),mButton.get(18));
                break;
            case R.id.bs02:
                secStr =  view.getTag().toString();
                closeMenu();
                isOpen = false;
                btn_animation(mButton.get(79),mButton.get(80));
                break;
            case R.id.bs021:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(79),mButton.get(80));
                break;
            case R.id.bs022:
                thrStr =  view.getTag().toString();
                btn_animationClose(mButton.get(79),mButton.get(80));
                break;
            case R.id.bs03:
                secStr =  view.getTag().toString();
                closeMenu();
                isOpen = false;
                btn_animation(mButton.get(79),mButton.get(80));
                break;
            case R.id.bs04:
                secStr =  view.getTag().toString();
                closeMenu();
                isOpen = false;
                btn_animation(mButton.get(79),mButton.get(80));
                break;
            case R.id.bd02:
                secStr =  view.getTag().toString();
                closeMenu();
                isOpen = false;
                btn_animation(mButton.get(17),mButton.get(18));
                break;
            case R.id.bd03:
                secStr =  view.getTag().toString();
                closeMenu();
                isOpen = false;
                btn_animation(mButton.get(79),mButton.get(80));
                break;
            case R.id.fix01:
                secStr =  view.getTag().toString();
                thrStr = "   ";
                closeMenu();
                isOpen = false;
                break;
            case R.id.fix02:
                secStr =  view.getTag().toString();
                thrStr = "   ";
                closeMenu();
                isOpen = false;
                break;
            case R.id.fix03:
                secStr =  view.getTag().toString();
                thrStr = "   ";
                closeMenu();
                isOpen = false;
                break;
            case R.id.fix04:
                secStr =  view.getTag().toString();
                thrStr = "   ";
                closeMenu();
                isOpen = false;
                break;
            case R.id.fix05:
                secStr =  view.getTag().toString();
                thrStr = "   ";
                closeMenu();
                isOpen = false;
                break;
            case R.id.fix06:
                secStr =  view.getTag().toString();
                thrStr = "   ";
                closeMenu();
                isOpen = false;
                break;
        }

        if(firstStr.equals("銑鋪5cm")){
            if(secStr.equals("AC 滾壓")){
                totalstr=firstStr+" / AC厚度量測及黏層噴塗 / "+secStr+" / "+thrStr;
            }else if(thrStr.equals("厚度量測停檢全景")){
                totalstr=firstStr+" / "+secStr+" / 厚度(AC 5cm) / "+thrStr;
            }else if(thrStr.equals("近拍(箱尺、停檢白板)")){
                totalstr=firstStr+" / "+secStr+" / 厚度(AC 5cm) / "+thrStr;
            }else {
                totalstr=firstStr + " / " + secStr + " / " + thrStr;
            }
        }else {
            totalstr=firstStr + " / " + secStr + " / " + thrStr;
        }
        txtlabel.setText("目前拍攝項目: "+totalstr);
    }

    private void setAutoFlash(CaptureRequest.Builder requestBuilder) {
        if (mFlashSupported) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }

    /**
     * Saves a JPEG {@link Image} into the specified {@link File}.
     */
    private static class ImageSaver implements Runnable {

        /**
         * The JPEG image
         */
        private final Image mImage;
        /**
         * The file we save the image into.
         */
        private final File mFile;

        public ImageSaver(Image image, File file) {
            mImage = image;
            mFile = file;
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);

            try {
                if (mFile.exists()) mFile.delete();
                FileOutputStream output = new FileOutputStream(mFile);
                output.write(bytes);
                try {mImage.close();} catch (Exception ignored) {}
                try {output.close();} catch (Exception ignored) {}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Compares two {@code Size}s based on their areas.
     */
    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    /**
     * Shows an error message dialog.
     */
    public static class ErrorDialog extends DialogFragment {

        private static final String ARG_MESSAGE = "message";

        public static ErrorDialog newInstance(String message) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage(getArguments().getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    })
                    .create();
        }

    }

    public void scaleHeight(){
        screen_x = welcomePage.screen_x;
        screen_y = welcomePage.screen_y;
        center = screen_x/2;
        screen_y = screen_y/10;
        screen_x = screen_x/10;

    }


    private void openMenu() {
        int count = 0;
        int arraynum = 0;
        thrStr="-";
        secStr="-";
        //Log.d("MYLOD","count: "+ACbtn.length+"");
        switch(clayer) {
            case 0:{
            arraynum = ((mRes.length -1)*2)+1;
            ObjectAnimator[] animators = new ObjectAnimator[arraynum];
            animators[0] = ObjectAnimator.ofFloat(mButton.get(0), "alpha", 1f, 1f);

            for (int zz = 1; zz < 8; zz++) {
                mButton.get(zz).setEnabled(true);
                animators[zz] = ObjectAnimator.ofFloat(mButton.get(zz), "translationY", 0, zz * screen_y);
                animators[zz + 7] = ObjectAnimator.ofFloat(mButton.get(zz), "alpha", 0f, 1f);
            }

            AnimatorSet set = new AnimatorSet();
            set.playTogether(animators);
            //set.setInterpolator(new BounceInterpolator());
            set.setDuration(50);
            set.start();
                break;
            }
            case 1:{
                switch (sslayer){
                    case 0:
                        open_animation(8,15,ACbtn);
                        break;
                    case 1:
                        open_animation(29,34,Sibtn);
                        break;
                    case 2:
                        open_animation(49,53,Finbtn);
                        break;
                    case 3:
                        open_animation(53,59,hubtn);
                        break;
                    case 4:
                        open_animation(71,77,Bsbtn);
                        break;
                    case 5:
                        open_animation(85,91,Bdbtn);
                        break;
                    case 6:
                        open_animation(103,109,Fixbtn);
                        break;
                }
                break;
            }
        }
    }

    private void open_animation(int start,int end,int[] btn_id){
        int counts = 0;
        AnimatorSet layerset = new AnimatorSet();
        ObjectAnimator[] layeranimator = new ObjectAnimator[btn_id.length * 2];
        for (int zz = start; zz < end; zz++) {
            mButton.get(zz).setEnabled(true);
            layeranimator[counts] = ObjectAnimator.ofFloat(mButton.get(zz), "translationY", 0, (counts+1) * screen_y);
            layeranimator[counts+btn_id.length] = ObjectAnimator.ofFloat(mButton.get(zz), "alpha", 0f, 1f);
            counts++;
        }
        layerset.playTogether(layeranimator);
        layerset.setDuration(350);
        layerset.start();
    }

    private void closeMenu() {
        int count = 1;
        int arraynum = 0;
        switch(clayer){
            case 0:
                arraynum = ((mRes.length -1)*2)+1;
                ObjectAnimator[] animators = new ObjectAnimator[arraynum];
                animators[0]=ObjectAnimator.ofFloat(mButton.get(0), "alpha", 0.5f, 1f);

                for(int zz = 1; zz<8 ;zz++){
                    animators[zz] = ObjectAnimator.ofFloat(mButton.get(zz), "translationX", 0, screen_x*10);
                    animators[zz+7] = ObjectAnimator.ofFloat(mButton.get(zz), "alpha", 1f,0f);
                    mButton.get(zz).setEnabled(false);
                    count++;
                }

                AnimatorSet set = new AnimatorSet();
                set.playTogether(animators);
                set.setDuration(300);
                set.start();
                break;

            case 1:
                switch(sslayer){
                    case 0:
                        close_animation(8,15,ACbtn);
                        break;
                    case 1:
                        close_animation(29,34,Sibtn);
                        break;
                    case 2:
                        close_animation(49,53,Finbtn);
                        break;
                    case 3:
                        close_animation(53,59,hubtn);
                        break;
                    case 4:
                        close_animation(71,77,Bsbtn);
                        break;
                    case 5:
                        close_animation(85,91,Bdbtn);
                        break;
                    case 6:
                        close_animation(103,109,Fixbtn);
                        break;

                }
                break;

                }
        }

    private void close_animation(int start,int end,int[] btn_id){
        int counts = 0;
        AnimatorSet layerset = new AnimatorSet();
        ObjectAnimator[] layeranimator = new ObjectAnimator[btn_id.length*2];
        for (int zz = start; zz < end; zz++) {
            mButton.get(zz).setEnabled(false);
            layeranimator[counts] = ObjectAnimator.ofFloat(mButton.get(zz), "translationY", counts * screen_y, screen_y*10 );
            layeranimator[counts+btn_id.length] = ObjectAnimator.ofFloat(mButton.get(zz), "alpha", 1f, 0f);
            counts++;
        }
        layerset.playTogether(layeranimator);
        layerset.setDuration(350);
        layerset.start();
    }


        public void btn_animation(Button ... items){
            AnimatorSet layerset = new AnimatorSet();
            ObjectAnimator[] layeranimator= new ObjectAnimator[(items.length*2)];
            if (items != null) {
                for (int zz = 0; zz < items.length; zz++) {
                    Log.d("MYLOG","zz: "+zz);
                    items[zz].setEnabled(true);
                    layeranimator[zz] = ObjectAnimator.ofFloat(items[zz], "translationY",0, (zz+1) * screen_y);
                    layeranimator[zz+items.length] = ObjectAnimator.ofFloat(items[zz], "alpha", 0f, 1f);
                }
                layerset.playTogether(layeranimator);
                layerset.setDuration(350);
                layerset.start();
            }
        }
    public void btn_animationClose(Button ... items){
        AnimatorSet layerset = new AnimatorSet();
        ObjectAnimator[] layeranimator= new ObjectAnimator[(items.length*2)];
        if (items != null) {
            for (int zz = 0; zz < items.length; zz++) {
                Log.d("MYLOG","zz: "+zz);
                items[zz].setEnabled(true);
                layeranimator[zz] = ObjectAnimator.ofFloat(items[zz], "translationY",(zz+1) * screen_y, -10*screen_y);
                layeranimator[zz+items.length] = ObjectAnimator.ofFloat(items[zz], "alpha", 1f, 0f);
            }
            layerset.playTogether(layeranimator);
            layerset.setDuration(350);
            layerset.start();
        }
    }




    }
