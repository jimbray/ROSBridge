package xyz.jimbray.rosbridge.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import xyz.jimbray.rosbridge.BaseFragment;
import xyz.jimbray.rosbridge.R;
import xyz.jimbray.rosbridge.activities.MainActivity;
import xyz.jimbray.rosbridge.contracts.ConnectingContract;
import xyz.jimbray.rosbridge.presenters.ConnectingPresenter;

public class ConnectingFragment extends BaseFragment implements ConnectingContract.IConnectingView {

    private ConnectingContract.IConnectingPresenter mPresenter;

    private EditText et_url, et_port;

    private Button btn_connect;

    private RelativeLayout layout_connecting;

    private TextInputLayout layout_et_url, layout_et_port;

    private TextView tv_spilliter_url_port;


    public static Fragment newInstanece() {
        Fragment fg = new ConnectingFragment();
        fg.setHasOptionsMenu(true);

        return fg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new ConnectingPresenter(this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fg_connecing, container, false);

        initViews(view);
        return view;
    }

    @Override
    protected void initViews(View view) {

        et_url = view.findViewById(R.id.et_url);
        et_port = view.findViewById(R.id.et_port);

        layout_et_url = view.findViewById(R.id.layout_et_url);
        layout_et_port = view.findViewById(R.id.layout_et_port);
        layout_connecting = view.findViewById(R.id.layout_connecting);
        tv_spilliter_url_port = view.findViewById(R.id.tv_spilliter_url_port);


        btn_connect = view.findViewById(R.id.btn_connect);

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_url.getText()) || TextUtils.isEmpty(et_port.getText())) {
                    Toast.makeText(getActivity(), R.string.connect_info_invalid, Snackbar.LENGTH_LONG).show();
                } else {
                    mPresenter.connect2ROS(et_url.getText().toString(), Integer.parseInt(et_port.getText().toString()));
                    showConnectingView();
                }
            }
        });
    }


    private void showConnectingView() {
        layout_connecting.setVisibility(View.VISIBLE);

        layout_et_url.setVisibility(View.GONE);
        layout_et_port.setVisibility(View.GONE);
        tv_spilliter_url_port.setVisibility(View.GONE);
        btn_connect.setVisibility(View.GONE);
    }

    private void showConnectingInfoView() {
        layout_connecting.setVisibility(View.GONE);

        layout_et_url.setVisibility(View.VISIBLE);
        layout_et_port.setVisibility(View.VISIBLE);
        tv_spilliter_url_port.setVisibility(View.VISIBLE);
        btn_connect.setVisibility(View.VISIBLE);

    }

    private void showROSOperationInfo() {
        if ((MainActivity)getActivity() != null) {
            ((MainActivity)getActivity()).switchFragment(RosPannelFragment.newInstanece());
        }
    }


    @Override
    public void rosConnected() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showROSOperationInfo();
                }
            });
        }
    }

    @Override
    public void rosDisconnected() {
        if (getActivity() == null) {
            return ;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showConnectingInfoView();
            }
        });
    }

    @Override
    public void rosConnectError(Exception ex) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showConnectingInfoView();
            }
        });
    }

    @Override
    public String getUrl() {
        return et_url.getText().toString();
    }

    @Override
    public int getPort() {
        return Integer.parseInt(et_port.getText().toString());
    }

    @Override
    public void setPresenter(ConnectingContract.IConnectingPresenter presenter) {
        this.mPresenter = presenter;
    }
}
