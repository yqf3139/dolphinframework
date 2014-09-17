package seu.lab.dolphinframework.fragment;

import java.util.List;

import seu.lab.dolphin.dao.Plugin;
import seu.lab.dolphin.server.DaoManager;
import android.app.ListFragment;  
import android.os.Bundle;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import seu.lab.dolphinframework.R;

public class TabExpansion extends ListFragment {  
  
    private TabExpansionAdapter adapter = null;
    public static List<Plugin> plugins = null;
      
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        
        adapter = new TabExpansionAdapter(getActivity());  
        setListAdapter(adapter);
        plugins = DaoManager.getDaoManager(getActivity()).listAllPlugins();
        adapter.add(plugins);
        
    }  
  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View v = inflater.inflate(R.layout.expansion, container, false);  
        return v;  
    }
    
    @Override
    public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	adapter.notifyDataSetChanged();
    }
    
}  
