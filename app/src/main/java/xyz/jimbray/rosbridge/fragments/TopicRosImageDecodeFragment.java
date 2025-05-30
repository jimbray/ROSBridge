package xyz.jimbray.rosbridge.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import xyz.jimbray.rosbridge.R;
import xyz.jimbray.rosbridge.messages.ITopicNames;
import xyz.jimbray.rosbridge.messages.RosImageData;

/**
 * Created by jimbray on 2018/10/31.
 * Email: jimbray16@gmail.com
 */

public class TopicRosImageDecodeFragment extends ImageDecoderFragment {

    //private ImageView iv_data;
    private SurfaceView sv_data;
    private SurfaceHolder mSurfaceHolder;

    private Button btn_subscribe, btn_unsubscribe;

    private boolean isSurfaceReady = false;

    public static String KEY_TOPIC = "key_topic";

    public static RosPannelTopticBaseFragment newInstance(String topic) {
        RosPannelTopticBaseFragment fg = new TopicRosImageDecodeFragment();
        fg.setHasOptionsMenu(true);

        Bundle bundle = new Bundle();
        bundle.putString(KEY_TOPIC, topic);

        fg.setArguments(bundle);

        return fg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurTopic = getArguments().getString(KEY_TOPIC);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fg_image_decode, container, false);
        initViews(view);
        return view;
    }

    @Override
    protected void initViews(View view) {
        //iv_data = view.findViewById(R.id.iv_data);
        sv_data = view.findViewById(R.id.sv_data);
        btn_subscribe = view.findViewById(R.id.btn_subscribe);
        btn_unsubscribe = view.findViewById(R.id.btn_unsubscribe);

        btn_subscribe.setOnClickListener(this);
        btn_unsubscribe.setOnClickListener(this);

        mSurfaceHolder = sv_data.getHolder();
        sv_data.setKeepScreenOn(true);

        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                isSurfaceReady = true;

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                synchronized (this) {
                    isSurfaceReady = false;
                    mSurfaceHolder.removeCallback(this);

                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_subscribe:
                mPresenter.subscribeTopic(mCurTopic);
                break;

            case R.id.btn_unsubscribe:
                mPresenter.unSubscribeTopic(mCurTopic);
                break;
        }
    }

    @Override
    protected void setImage(RosImageData imageData) {
        if (TextUtils.isEmpty(imageData.data)) {
            return;
        }

        final byte[] image_byte_array = Base64.decode(imageData.data, Base64.NO_PADDING);


        if (isSurfaceReady) {


            synchronized (mSurfaceHolder) {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap bmp = BitmapFactory.decodeByteArray(image_byte_array, 0, image_byte_array.length, null);

                // 修改显示大小
                // camera右侧3/4的宽度拿来显示图像，?
                float targetWidth = 0f;
                float targetHeight = 0f;
                if (bmp.getWidth() > bmp.getHeight()) { // 横图
                    targetWidth = sv_data.getMeasuredWidth(); // 使用宽度定位
                    targetHeight = bmp.getHeight()*targetWidth/bmp.getWidth(); //由宽度决定高度（等比缩放）
                } else { // 竖图或方图
                    targetHeight = sv_data.getMeasuredHeight(); // 使用高度定位
                    targetWidth = bmp.getWidth()*targetHeight/bmp.getHeight();//由宽度决定高度（等比缩放）
                }

                // 取得想要缩放的matrix參數
                Matrix matrix = new Matrix();
                matrix.postScale(targetWidth/bmp.getWidth(), targetHeight/bmp.getHeight());
                // 得到新的圖片
                Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix,true);

                Canvas canvas = mSurfaceHolder.lockCanvas();

                try {
                    if (canvas != null) {
                        canvas.drawBitmap(newbm, 0, 0, null);
                    }
                } catch (Exception e) {

                } finally {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                    if (bmp != null) {
                        bmp.recycle();
                    }

                    if (newbm != null) {
                        newbm.recycle();
                    }

                    Log.d("jimjim", "finished Drawing...");
                }


            }

        }


    }

    @Override
    protected void setImage(String base64Str) {
        final byte[] image_byte_array = Base64.decode(base64Str, Base64.DEFAULT | Base64.NO_WRAP);

        if (isSurfaceReady) {
            synchronized (mSurfaceHolder) {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap bmp = BitmapFactory.decodeByteArray(image_byte_array, 0, image_byte_array.length, options);


                // 修改显示大小
                // camera右侧3/4的宽度拿来显示图像，?
                float targetWidth = 0f;
                float targetHeight = 0f;
                if (bmp.getWidth() > bmp.getHeight()) { // 横图
                    targetWidth = sv_data.getMeasuredWidth(); // 使用宽度定位
                    targetHeight = bmp.getHeight()*targetWidth/bmp.getWidth(); //由宽度决定高度（等比缩放）
                } else { // 竖图或方图
                    targetHeight = sv_data.getMeasuredHeight(); // 使用高度定位
                    targetWidth = bmp.getWidth()*targetHeight/bmp.getHeight();//由宽度决定高度（等比缩放）
                }

                // 取得想要缩放的matrix參數
                Matrix matrix = new Matrix();
                matrix.postScale(targetWidth/bmp.getWidth(), targetHeight/bmp.getHeight());
                // 得到新的圖片
                Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix,true);

                Canvas canvas = mSurfaceHolder.lockCanvas();

                try {
                    if (canvas != null) {
                        canvas.drawBitmap(newbm, 0, 0, null);
                    }
                } catch (Exception e) {

                } finally {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                    if (bmp != null) {
                        bmp.recycle();
                    }

                    if (newbm != null) {
                        newbm.recycle();
                    }

                    Log.d("jimjim", "finished Drawing...");
                }


            }

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.unSubscribeTopic(mCurTopic);
    }
}
