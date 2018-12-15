package com.zain.fragmentcommunicationinterface;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FragmentB extends Fragment {
    private FragmentBListener listener;
    private EditText editText;
    private Button buttonOk;
    private Button button_share_view_model;
    private ShareViewModel shareViewModel;
    public interface FragmentBListener {
        void onInputBSent(CharSequence input);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_b, container, false);

        editText = v.findViewById(R.id.edit_text);
        buttonOk = v.findViewById(R.id.button_ok);
        button_share_view_model = v.findViewById(R.id.button_share_view_model);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence input = editText.getText();
                listener.onInputBSent(input);
            }
        });
        button_share_view_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareViewModel.setText(editText.getText());
            }
        });
        return v;
    }
    public void updateEditText(CharSequence newText) {
        editText.setText(newText);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentBListener) {
            listener = (FragmentBListener) context;/*context means the activity
            let this fragment is our presenter in MVP and in the presenter constructor (fragment's onAttach)
            needs to take an set the view

            ex :class {
            View mview;
           public presenterImpl(View view){
           this.mView - view;} the same like listener = context; context is our activity ; but we need to cast it when it implements
           the  current fragment

             validate(){
             mView.display()} = listener.onInputASent(input);
         }
           */
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentBListener");
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        shareViewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);
        shareViewModel.getText().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(@Nullable CharSequence charSequence) {
                editText.setText(charSequence);
            }
        });
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
