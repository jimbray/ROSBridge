package xyz.jimbray.rosbridge.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class TopicRosImageDecodeFragment extends RosPannelTopticBaseFragment implements View.OnClickListener {

    //private ImageView iv_data;
    private SurfaceView sv_data;
    private SurfaceHolder mSurfaceHolder;

    private Button btn_subscribe, btn_unsubscribe;

    private boolean isSurfaceReady = false;

    private boolean iscanDraw = false;

    public static RosPannelTopticBaseFragment newInstance() {
        RosPannelTopticBaseFragment fg = new TopicRosImageDecodeFragment();
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
                mPresenter.subscribeTopic(ITopicNames.CAMERA_IMAGE_TEST);
                break;

            case R.id.btn_unsubscribe:
                mPresenter.unSubscribeTopic(ITopicNames.CAMERA_IMAGE_TEST);
                break;
        }
    }


    public void setImage(String image_str) {

        /*
        if (TextUtils.isEmpty(image_str)) {
            return;
        }
        final byte[] image_byte_array = Base64.decode(image_str, Base64.DEFAULT);

        if (image_str.equals(oldBase64Str)) {
            return ;
        }

        if (iscanDraw) {
            return;
        }


        oldBase64Str = image_str;
        //btn_unsubscribe.performClick();

        if (isSurfaceReady) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    synchronized (mSurfaceHolder) {

                        Log.d("jimjim", "start Drawing...");
                        iscanDraw = false;

                        mPresenter.unSubscribeTopic(ITopicNames.IMAGE_BASE64_STR);

                        Bitmap bmp = BitmapFactory.decodeByteArray(image_byte_array, 0, image_byte_array.length, null);
                        Canvas canvas = mSurfaceHolder.lockCanvas();
                        try {
                            if (canvas != null) {
                                canvas.drawBitmap(bmp, 0, 0, null);
                            }
                        } catch (Exception e) {

                        } finally {
                            mSurfaceHolder.unlockCanvasAndPost(canvas);
                            bmp.recycle();
                            iscanDraw = true;

                            mPresenter.subscribeTopic(ITopicNames.IMAGE_BASE64_STR);
                            Log.d("jimjim", "finished Drawing...");
                        }
                    }

                }
            }).start();


        }
        */





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
