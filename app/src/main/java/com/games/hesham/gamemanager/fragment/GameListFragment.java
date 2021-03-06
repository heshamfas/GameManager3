package com.games.hesham.gamemanager.fragment;

import com.games.hesham.gamemanager.ActionItemClickedListener;
import com.games.hesham.gamemanager.ActivityType;
import com.games.hesham.gamemanager.GameManager;
import com.games.hesham.gamemanager.R;
import com.games.hesham.gamemanager.activity.ListGamesActivity;
import com.games.hesham.gamemanager.adapter.GameAdapter;
import com.games.hesham.gamemanager.object.GameObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class GameListFragment extends Fragment implements ActionItemClickedListener ,
        View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView gameList;
    private ActivityType activityType;
    private EditText gameNameET;
    private EditText consoleET;
    private EditText imageUrlET;
    private Button addBtn;
    private Button cancelBtn;
    private LinearLayout addLayout;
    private GameAdapter adapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameListFragment newInstance(String param1, String param2) {
        GameListFragment fragment = new GameListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public GameListFragment() {
        // Required empty public constructor
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
        View view =inflater.inflate(R.layout.fragment_game_list, container, false);
        addLayout = (LinearLayout)view.findViewById(R.id.addLL);
        gameList = (ListView)view.findViewById(R.id.game_listLV);
        gameNameET = (EditText)view.findViewById(R.id.list_add_game_nameET);
        consoleET = (EditText)view.findViewById(R.id.list_add_console_nameET);
         imageUrlET = (EditText)view.findViewById(R.id.game_imageET);
        addBtn = (Button)view.findViewById(R.id.add_btn);
        addBtn.setOnClickListener(this);
        cancelBtn= (Button)view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(this);
        LinearLayout addLayout= (LinearLayout)view.findViewById(R.id.addLL);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        resetGameList();

    }

    // TODO: Rename method, update argument and hook method into UI event
/*    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            Activity a = getActivity();
            //mListener = (OnFragmentInteractionListener) activity;
            if(a instanceof ListGamesActivity) {
                activityType = ActivityType.LIST;
                ListGamesActivity.addActionItemClickListener(this);
            }else {
                activityType = ActivityType.RATING;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void onActionItemClicked(MenuItem item) {
        if(item.getItemId() ==R.id.action_add){
            // do stuff for adding the item
            showAddItemLayout();

        }
    }

    private void showAddItemLayout() {
        gameList.setVisibility(View.GONE);
        addLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_btn:
                GeneralDialogFragment generalDialogFragment = new GeneralDialogFragment();
                    Bundle bundle = new Bundle();
                if(TextUtils.isEmpty(gameNameET.getText().toString())){
                     showGeneralDialog("Error", "Missing Game Name");
                }else if(TextUtils.isEmpty(consoleET.getText().toString())) {
                    showGeneralDialog("Error", "Missing Console");
                }else if(TextUtils.isEmpty(imageUrlET.getText().toString())){
                showGeneralDialog("Error", "Missing Image Url");
            }else {
                    GameObject gameObject = new GameObject(gameNameET.getText().toString(),consoleET.getText().toString(),imageUrlET.getText().toString(), false);
                    GameManager.getGameObjects().add(gameObject);
                    gameList.setVisibility(View.VISIBLE);
                    resetGameList();
                    addLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.cancel_btn:
                gameList.setVisibility(View.VISIBLE);
                addLayout.setVisibility(View.GONE);
                break;
        }
    }
private void resetGameList(){
    adapter = new GameAdapter(getActivity(),GameManager.getGameObjects(), activityType);
    adapter.notifyDataSetChanged();
    gameList.setAdapter(adapter);
}
    private void showGeneralDialog(String title, String message){
        GeneralDialogFragment generalDialogFragment = new GeneralDialogFragment();
        Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("message", message);
            generalDialogFragment.setArguments(bundle);
            generalDialogFragment.show(getFragmentManager(), "");

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
        public void onFragmentInteraction(Uri uri);

    }

}
