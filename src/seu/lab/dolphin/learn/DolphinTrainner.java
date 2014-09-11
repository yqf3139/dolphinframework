package seu.lab.dolphin.learn;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;

import seu.lab.dolphin.client.DolphinCoreVariables;
import seu.lab.dolphin.dao.Gesture;
import seu.lab.dolphin.dao.Model;
import seu.lab.dolphin.dao.ModelConfig;
import seu.lab.dolphin.dao.RawGestureData;
import seu.lab.dolphin.dao.TrainingDataset;
import seu.lab.dolphin.dao.TrainingRelation;
import seu.lab.dolphin.server.DaoManager;
import android.R.integer;
import android.content.Context;

public class DolphinTrainner {
	
	public static JSONArray createModel(String modelpath, Gesture[] gestures) throws IOException{
		JSONArray output = new JSONArray();
		
		List<Double> lables = new LinkedList<Double>();
		List<Feature[]> features = new LinkedList<Feature[]>();
		
		for (int i = 0; i < gestures.length; i++) {
			output.put(gestures[i].getId());
			List<RawGestureData> rawGestureDatas = gestures[i].getRaw_gesuture_data();
			for (int j = 0; j < rawGestureDatas.size(); j++) {
				lables.add((double) i);
				double[] tweakedData = DaoManager.toDoubleArray(rawGestureDatas.get(i).getData());
				Feature[] nodes = new FeatureNode[tweakedData.length];
				for (int k = 0; k < tweakedData.length; k++) {
					nodes[k] = new FeatureNode(k+1,tweakedData[k]);
				}
				features.add(nodes);
			}
		}
		
		Parameter parameter = new Parameter(SolverType.L2R_L2LOSS_SVC_DUAL, 1.0, 0.01);
		
		Problem problemA = new Problem();
		problemA.l = lables.size();
		problemA.n = 2*30*30;
		problemA.y = new double[lables.size()];
		problemA.x = new Feature[lables.size()][];
		problemA.bias = 1.0;
		
		for (int i1 = 0; i1 < lables.size(); i1++) {
			problemA.y[i1] = lables.get(i1);
			problemA.x[i1] = features.get(i1);
		}
		
		de.bwaldvogel.liblinear.Model modelA = Linear.train(problemA, parameter);

		for (int i1 = 0; i1 < features.size(); i1++) {
			double prediction = Linear.predict(modelA, features.get(i1));
			System.out.println(lables.get(i1)+" -> "+prediction);
		}
		
		modelA.save(new File(DolphinCoreVariables.DOLPHIN_HOME+"models/"+modelpath));
		
		return output;
	}

}
