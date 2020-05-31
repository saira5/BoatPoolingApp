package com.example.boatpoolingapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.boatpoolingapp.R;
import com.example.boatpoolingapp.model.Message;
import com.example.boatpoolingapp.utils.ChatBoxAdapter;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link inboxFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link inboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class inboxFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Socket socket;
    private String Nickname ;
    public RecyclerView myRecylerView ;
    public List<Message> MessageList ;
    public ChatBoxAdapter chatBoxAdapter;
    public EditText messagetxt ;
    public Button send ;
    private OnFragmentInteractionListener mListener;

    public inboxFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment inboxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static inboxFragment newInstance(String param1, String param2) {
        inboxFragment fragment = new inboxFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_inbox, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try{
        socket = IO.socket("http://192.168.10.8:3000");
        socket.connect();
        socket.emit("join", Nickname);

    } catch (URISyntaxException e) {
        e.printStackTrace();

    }

        MessageList = new ArrayList<>();
        myRecylerView = (RecyclerView) view.findViewById(R.id.messagelist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());

        messagetxt = (EditText) view.findViewById(R.id.message) ;
        send = (Button) view.findViewById(R.id.send);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetectionif(!messagetxt.getText().toString().isEmpty()){
                socket.emit("messagedetection",Nickname,messagetxt.getText().toString());

                messagetxt.setText(" ");
            }


        } );

        //implementing socket listeners
        socket.on("userjoinedthechat", new Emitter.Listener() {
            @Override public void call(final Object... args) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        String data = (String) args[0];

                        Toast.makeText(getContext(),data,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        socket.on("userdisconnect", new Emitter.Listener() {
            @Override public void call(final Object... args) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(getContext(),data,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event

                            String nickname = data.getString("senderNickname");
                            String message = data.getString("message");

                            // make instance of message

                            Message m = new Message(nickname,message);


                            //add the message to the messageList

                            MessageList.add(m);

                            // add the new updated list to the dapter
                            chatBoxAdapter = new ChatBoxAdapter(MessageList);

                            // notify the adapter to update the recycler view

                            chatBoxAdapter.notifyDataSetChanged();

                            //set the adapter for the recycler view

                            myRecylerView.setAdapter(chatBoxAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
