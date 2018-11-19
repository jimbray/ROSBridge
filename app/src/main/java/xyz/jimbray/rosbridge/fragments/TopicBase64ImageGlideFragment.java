package xyz.jimbray.rosbridge.fragments;

import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import xyz.jimbray.rosbridge.R;
import xyz.jimbray.rosbridge.messages.ITopicNames;

/**
 * Created by jimbray on 2018/10/31.
 * Email: jimbray16@gmail.com
 */

@Deprecated
public class TopicBase64ImageGlideFragment extends ImageDecoderFragment {

    private ImageView iv_data;
    private SurfaceView sv_data;
    private SurfaceHolder mSurfaceHolder;

    private Button btn_subscribe, btn_unsubscribe;

    private String oldBase64Str = null;

    public static RosPannelTopticBaseFragment newInstance() {
        RosPannelTopticBaseFragment fg = new TopicBase64ImageGlideFragment();
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
        View view = inflater.inflate(R.layout.layout_fg_image_glide, container, false);
        initViews(view);
        return view;
    }

    @Override
    protected void initViews(View view) {
        iv_data = view.findViewById(R.id.iv_data);
        btn_subscribe = view.findViewById(R.id.btn_subscribe);
        btn_unsubscribe = view.findViewById(R.id.btn_unsubscribe);

        btn_subscribe.setOnClickListener(this);
        btn_unsubscribe.setOnClickListener(this);

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
        byte[] image_byte_array = Base64.decode(base64Str, Base64.NO_WRAP);

        if (base64Str.equals(oldBase64Str)) {
            return ;
        }


        oldBase64Str = base64Str;
        //RequestOptions options = new RequestOptions();
        //options.dontAnimate();
        Glide.with(getActivity())
                //      .applyDefaultRequestOptions(options)
                .load(image_byte_array)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.d("image", "load base64 image failed");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("image", "load base64 image successfuled");
                        return false;
                    }
                })
                .into(iv_data);
    }

}
