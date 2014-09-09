package seu.lab.dolphin.learn;

import java.util.List;

import seu.lab.dolphin.dao.Model;
import seu.lab.dolphin.dao.ModelConfig;
import seu.lab.dolphin.dao.TrainingDataset;
import seu.lab.dolphin.dao.TrainingRelation;
import android.content.Context;

public class DolphinTrainner {

	private Context mContext;

	public DolphinTrainner(Context context) {
		mContext = context;
	}
	
	void refreshModel(Model model){
		TrainingDataset dataset = model.getTrainingDataset();
		List<TrainingRelation> relations = dataset.getTraining_relation();
		
	}
	
	Model[] createModels(String maskString){

		return null;
	}

}
