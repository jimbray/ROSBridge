package xyz.jimbray.rosbridge.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

/**
 * Created by jimbray on 2018/10/31.
 * Email: jimbray16@gmail.com
 */

public class TopicBase64ImageDecodeFragment extends ImageDecoderFragment {

    private SurfaceView sv_data;
    private SurfaceHolder mSurfaceHolder;

    private Button btn_subscribe, btn_unsubscribe;

    private boolean isSurfaceReady = false;

    public static RosPannelTopticBaseFragment newInstance() {
        RosPannelTopticBaseFragment fg = new TopicBase64ImageDecodeFragment();
        fg.setHasOptionsMenu(true);

        return fg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                mPresenter.subscribeTopic(ITopicNames.IMAGE_BASE64_STR);
                break;

            case R.id.btn_unsubscribe:
                mPresenter.unSubscribeTopic(ITopicNames.IMAGE_BASE64_STR);
                break;
        }
    }


    @Override
    protected void setImage(String base64Str) {
        if (TextUtils.isEmpty(base64Str)) {
            return;
        }
        final byte[] image_byte_array = Base64.decode(base64Str, Base64.DEFAULT | Base64.NO_WRAP);

        if (isSurfaceReady) {

            synchronized (mSurfaceHolder) {

                Log.d("jimjim", "start Drawing...");

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap bmp = BitmapFactory.decodeByteArray(image_byte_array, 0, image_byte_array.length, options);
                Canvas canvas = mSurfaceHolder.lockCanvas();

                try {
                    if (canvas != null) {
                        canvas.drawBitmap(bmp, 0, 0, null);
                    }
                } catch (Exception e) {

                } finally {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                    if (bmp != null) {
                        bmp.recycle();
                    }

                    Log.d("jimjim", "finished Drawing...");
                }
            }

        }

    }


}
