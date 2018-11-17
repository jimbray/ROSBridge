package xyz.jimbray.rosbridge.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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

//    private CircleButton btn_forward, btn_backward;
    private CircleButton btn_left, btn_right, btn_forward, btn_backward;
    private Button btn_stop;

    private boolean isSurfaceReady = false;

    private Disposable cmdDisposable;

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
        RosBridgeClientManager.getInstance().advertiseTopic(ITopicNames.CPS_COMMAND, new RosStringData());
        RosBridgeClientManager.getInstance().advertiseTopic(ITopicNames.CMD_VEL, new RosStringData());

        surfaceView = view.findViewById(R.id.surfaceview);

//        btn_forward = view.findViewById(R.id.btn_forward);
//        btn_backward = view.findViewById(R.id.btn_backward);
//
//        btn_forward.setOnClickListener(this);
//        btn_backward.setOnClickListener(this);

        btn_left = view.findViewById(R.id.btn_left);
        btn_right = view.findViewById(R.id.btn_right);
        btn_forward = view.findViewById(R.id.btn_forward);
        btn_backward = view.findViewById(R.id.btn_backward);
        btn_stop = view.findViewById(R.id.btn_stop);

//        btn_left.setOnClickListener(this);
//        btn_right.setOnClickListener(this);
//        btn_forward.setOnClickListener(this);
//        btn_backward.setOnClickListener(this);

        btn_left.setOnTouchListener(touchListener);
        btn_right.setOnTouchListener(touchListener);
        btn_forward.setOnTouchListener(touchListener);
        btn_backward.setOnTouchListener(touchListener);
        btn_stop.setOnClickListener(this);

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
//            case R.id.btn_forward:
//                FakeCPSCommand command_foward = new FakeCPSCommand();
//                command_foward.setMobile_platform_move("foward");
//
//                FakeCPSCommand.Header header_forward = new FakeCPSCommand.Header();
//                header_forward.setSeq(0);
//                header_forward.setStamp(System.currentTimeMillis());
//                command_foward.setHeader(header_forward);
//
//                RosStringData stringDataForward = new RosStringData(new Gson().toJson(command_foward));
//                mPresenter.publishTopic(ITopicNames.CPS_COMMAND, stringDataForward);
//
//                mPresenter.publishTopic(ITopicNames.TURTLE1_CMD_VEL, turnLeft());
//                break;
//
//            case R.id.btn_backward:
//                FakeCPSCommand command_backward = new FakeCPSCommand();
//                command_backward.setMobile_platform_move("backward");
//
//
//                FakeCPSCommand.Header header_backward = new FakeCPSCommand.Header();
//                header_backward.setSeq(0);
//                header_backward.setStamp(System.currentTimeMillis());
//                command_backward.setHeader(header_backward);
//
//                RosStringData stringDataBackward = new RosStringData(new Gson().toJson(command_backward));
//                mPresenter.publishTopic(ITopicNames.CPS_COMMAND, stringDataBackward);
//                break;
            case R.id.btn_left:
                mPresenter.publishTopic(ITopicNames.CMD_VEL, turnLeft());
                //((RosPannelFragment)getParentFragment()).onPublishMessage("publish message -> left");
                break;

            case R.id.btn_right:
                mPresenter.publishTopic(ITopicNames.CMD_VEL, turnRight());
                //((RosPannelFragment)getParentFragment()).onPublishMessage("publish message -> right");
                break;

            case R.id.btn_forward:
                mPresenter.publishTopic(ITopicNames.CMD_VEL, forward());
                //((RosPannelFragment)getParentFragment()).onPublishMessage("publish message -> forward");
                break;

            case R.id.btn_backward:
                mPresenter.publishTopic(ITopicNames.CMD_VEL, backward());
                //((RosPannelFragment)getParentFragment()).onPublishMessage("publish message -> backward");
                break;

            case R.id.btn_stop:
                mPresenter.publishTopic(ITopicNames.CMD_VEL, stop());
                break;
        }
    }

    private TwistData forward() {
        TwistData twistData = new TwistData();
        twistData.linear.x = 0.1f;
        twistData.linear.y = 0.0f;
        twistData.linear.y = 0.0f;

        twistData.angular.x = 0.0f;
        twistData.angular.y = 0.0f;
        twistData.angular.z = 0.0f;

        return twistData;
    }


    private TwistData turnLeft() {

        TwistData twistData = new TwistData();
        twistData.linear.x = 0.0f;
        twistData.linear.y = 0.0f;
        twistData.linear.y = 0.0f;

        twistData.angular.x = 0.0f;
        twistData.angular.y = 0.0f;
        twistData.angular.z = 0.3f;

        return twistData;
    }

    private TwistData turnRight() {

        TwistData twistData = new TwistData();
        twistData.linear.x = 0.3f;
        twistData.linear.y = 0.0f;
        twistData.linear.y = 0.0f;

        twistData.angular.x = 0.0f;
        twistData.angular.y = 0.0f;
        twistData.angular.z = -0.3f;

        return twistData;
    }

    private TwistData backward() {

        TwistData twistData = new TwistData();
        twistData.linear.x = -0.3f;
        twistData.linear.y = 0.0f;
        twistData.linear.y = 0.0f;

        twistData.angular.x = 0.0f;
        twistData.angular.y = 0.0f;
        twistData.angular.z = 0.0f;

        return twistData;
    }

    private TwistData stop() {

        TwistData twistData = new TwistData();
        twistData.linear.x = 0.0f;
        twistData.linear.y = 0.0f;
        twistData.linear.y = 0.0f;

        twistData.angular.x = 0.0f;
        twistData.angular.y = 0.0f;
        twistData.angular.z = 0.0f;

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


    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                if (cmdDisposable != null) {
                    cmdDisposable.dispose();
                }
                switch (view.getId()) {
                    case R.id.btn_left:
                        Observable.interval(1, TimeUnit.SECONDS)
                                .subscribe(new Observer<Long>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        cmdDisposable = d;
                                    }

                                    @Override
                                    public void onNext(Long aLong) {
                                        mPresenter.publishTopic(ITopicNames.CMD_VEL, turnLeft());
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                        break;

                    case R.id.btn_right:
                        Observable.interval(1, TimeUnit.SECONDS)
                                .subscribe(new Observer<Long>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        cmdDisposable = d;
                                    }

                                    @Override
                                    public void onNext(Long aLong) {
                                        mPresenter.publishTopic(ITopicNames.CMD_VEL, turnRight());
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                        break;

                    case R.id.btn_forward:
                        Observable.interval(1, TimeUnit.SECONDS)
                                .subscribe(new Observer<Long>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        cmdDisposable = d;
                                    }

                                    @Override
                                    public void onNext(Long aLong) {
                                        mPresenter.publishTopic(ITopicNames.CMD_VEL, forward());
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                        break;

                    case R.id.btn_backward:
                        Observable.interval(1, TimeUnit.SECONDS)
                                .subscribe(new Observer<Long>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        cmdDisposable = d;
                                    }

                                    @Override
                                    public void onNext(Long aLong) {
                                        mPresenter.publishTopic(ITopicNames.CMD_VEL, backward());
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                        break;
                }
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (cmdDisposable != null) {
                    cmdDisposable.dispose();
                }
            }

            return true;
        }
    };


}
