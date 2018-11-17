package xyz.jimbray.rosbridge.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xyz.jimbray.rosbridge.R;
import xyz.jimbray.rosbridge.customviews.CircleButton;
import xyz.jimbray.rosbridge.managers.RosBridgeClientManager;
import xyz.jimbray.rosbridge.messages.FakeCPSCommand;
import xyz.jimbray.rosbridge.messages.ITopicNames;
import xyz.jimbray.rosbridge.messages.RosImageData;
import xyz.jimbray.rosbridge.messages.RosStringData;
import xyz.jimbray.rosbridge.messages.TwistData;

/**
 * Created by jimbray on 2018/10/31.
 * Email: jimbray16@gmail.com
 */

public class CpsClientDemoFragment extends RosPannelTopticBaseFragment implements View.OnClickListener {

    private SurfaceView surfaceView;
    private SurfaceHolder mSurfaceHolder;

    private CircleButton btn_forward, btn_backward;

    private boolean isSurfaceReady = false;

    public static RosPannelTopticBaseFragment newInstance() {
        RosPannelTopticBaseFragment fg = new CpsClientDemoFragment();
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
        View view = inflater.inflate(R.layout.layout_fg_cps_client, container, false);
        initViews(view);
        return view;
    }

    @Override
    protected void initViews(View view) {
        //iv_data = view.findViewById(R.id.iv_data);
        surfaceView = view.findViewById(R.id.surfaceview);
        btn_forward = view.findViewById(R.id.btn_forward);
        btn_backward = view.findViewById(R.id.btn_backward);

        RosBridgeClientManager.getInstance().advertiseTopic(ITopicNames.CPS_COMMAND, new RosStringData());

        btn_forward.setOnClickListener(this);
        btn_backward.setOnClickListener(this);

        mSurfaceHolder = surfaceView.getHolder();
        surfaceView.setKeepScreenOn(true);

        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                isSurfaceReady = true;


                subscribeClientTopic();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                synchronized (this) {
                    isSurfaceReady = false;
                    mSurfaceHolder.removeCallback(this);

                    ubsubscribeClientTopic();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forward:
                FakeCPSCommand command_foward = new FakeCPSCommand();
                command_foward.setMobile_platform_move("foward");

                FakeCPSCommand.Header header_forward = new FakeCPSCommand.Header();
                header_forward.setSeq(0);
                header_forward.setStamp(System.currentTimeMillis());
                command_foward.setHeader(header_forward);

                RosStringData stringDataForward = new RosStringData(new Gson().toJson(command_foward));
                mPresenter.publishTopic(ITopicNames.CPS_COMMAND, stringDataForward);

                mPresenter.publishTopic(ITopicNames.TURTLE1_CMD_VEL, turnLeft());
                break;

            case R.id.btn_backward:
                FakeCPSCommand command_backward = new FakeCPSCommand();
                command_backward.setMobile_platform_move("backward");


                FakeCPSCommand.Header header_backward = new FakeCPSCommand.Header();
                header_backward.setSeq(0);
                header_backward.setStamp(System.currentTimeMillis());
                command_backward.setHeader(header_backward);

                RosStringData stringDataBackward = new RosStringData(new Gson().toJson(command_backward));
                mPresenter.publishTopic(ITopicNames.CPS_COMMAND, stringDataBackward);
                break;
        }
    }

    private TwistData turnLeft() {

        TwistData twistData = new TwistData();
        twistData.linear.x = 2.0f;
        twistData.linear.y = 0.0f;
        twistData.linear.y = 0.0f;

        twistData.angular.x = 0.0f;
        twistData.angular.y = 0.0f;
        twistData.angular.z = 1.5f;

        return twistData;
    }



    public void setImage(final RosImageData imageData) {

        if (TextUtils.isEmpty(imageData.data)) {
            return;
        }
        final byte[] image_byte_array = Base64.decode(imageData.data, Base64.DEFAULT | Base64.NO_WRAP);


        if (isSurfaceReady) {

            Observable.create(new ObservableOnSubscribe<Bitmap>() {
                @Override
                public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
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
                        emitter.onNext(bmp);
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Bitmap>() {
                        @Override
                        public void accept(Bitmap bmp) throws Exception {

                            int width = bmp.getWidth();
                            int height = bmp.getHeight();

                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
                            lp.addRule(RelativeLayout.CENTER_VERTICAL);
                            surfaceView.setLayoutParams(lp);

                            if (bmp != null) {
                                bmp.recycle();
                            }
                        }
                    });



        }
    }

    private void subscribeClientTopic() {
        mPresenter.subscribeTopic(ITopicNames.CAMERA_IMAGE_COMPRESSED);
        mPresenter.subscribeTopic(ITopicNames.CPS_STATUS);
    }

    private void ubsubscribeClientTopic() {
        mPresenter.unSubscribeTopic(ITopicNames.CAMERA_IMAGE_COMPRESSED);
        mPresenter.unSubscribeTopic(ITopicNames.CPS_STATUS);
    }

}
