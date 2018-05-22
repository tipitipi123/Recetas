package ExplainInit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tipix1998.mainactivity.R;
import com.example.tipix1998.mainactivity.RecetasAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_pop_up_explain)
public class PopUpExplain extends DialogFragment{

  @ViewById
  RecyclerView rvExplain;
  PopUpExplainAdapter adapter;

  @AfterViews
  void afterViews(){
      adapter = new PopUpExplainAdapter();
      rvExplain.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false ) );
      rvExplain.setAdapter( adapter );
  }
}
