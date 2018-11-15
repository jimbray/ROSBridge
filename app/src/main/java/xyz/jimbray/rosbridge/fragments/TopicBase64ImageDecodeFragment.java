package xyz.jimbray.rosbridge.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
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

import java.io.ByteArrayOutputStream;

import xyz.jimbray.rosbridge.R;
import xyz.jimbray.rosbridge.messages.ITopicNames;

/**
 * Created by jimbray on 2018/10/31.
 * Email: jimbray16@gmail.com
 */

public class TopicBase64ImageDecodeFragment extends RosPannelTopticBaseFragment implements View.OnClickListener {

    //private ImageView iv_data;
    private SurfaceView sv_data;
    private SurfaceHolder mSurfaceHolder;

    private Button btn_subscribe, btn_unsubscribe;

    private String oldBase64Str = null;

    private boolean isSurfaceReady = false;

    private boolean iscanDraw = false;

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
                    iscanDraw = false;
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


    public void setImage(final String image_str) {
        if (TextUtils.isEmpty(image_str)) {
            return;
        }
        final byte[] image_byte_array = Base64.decode(image_str, Base64.DEFAULT | Base64.NO_WRAP);



        //if (image_str.equals(oldBase64Str)) {
        //    return ;
        //}

        // if (iscanDraw) {
        //    return;
        // }


        oldBase64Str = image_str;
        //btn_unsubscribe.performClick();

        if (isSurfaceReady) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    synchronized (mSurfaceHolder) {

                        Log.d("jimjim", "start Drawing...");
                        iscanDraw = false;

                        //mPresenter.unSubscribeTopic(ITopicNames.IMAGE_BASE64_STR);

                        // YuvImage yuvimage=new YuvImage(image_byte_array, ImageFormat.NV21, 640, 480, null);
                        // ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        // yuvimage.compressToJpeg(new Rect(0, 0, 640, 480), 80, baos);  //这里 80 是图片质量，取值范围 0-100，100为品质最高
                        // byte[] jdata = baos.toByteArray();

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        Bitmap bmp = BitmapFactory.decodeByteArray(image_byte_array, 0, image_byte_array.length, options);
                        Canvas canvas = mSurfaceHolder.lockCanvas();

                        Log.e("jim", "bmp is null !!!");
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
                            iscanDraw = true;

                            //mPresenter.subscribeTopic(ITopicNames.IMAGE_BASE64_STR);
                            Log.d("jimjim", "finished Drawing...");
                        }
                    }

                }
            }).start();


        }

        /*
        glide 显示图片
        //RequestOptions options = new RequestOptions();
        //options.dontAnimate();
        Glide.with(getActivity())
          //      .applyDefaultRequestOptions(options)
                .load(image_byte_array)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        btn_subscribe.performClick();
                        Log.d("image", "load base64 image failed");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        btn_subscribe.performClick();
                        Log.d("image", "load base64 image successfuled");
                        return false;
                    }
                })
                .into(iv_data);
                */
    }

}
