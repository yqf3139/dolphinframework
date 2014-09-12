package seu.lab.dolphin.learn;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
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
import seu.lab.dolphin.client.GestureData.RichFeatureData;
import seu.lab.dolphin.core.CoreSettings;
import seu.lab.dolphin.dao.Gesture;
import seu.lab.dolphin.dao.Model;
import seu.lab.dolphin.dao.ModelConfig;
import seu.lab.dolphin.dao.RawGestureData;
import seu.lab.dolphin.dao.TrainingDataset;
import seu.lab.dolphin.dao.TrainingRelation;
import seu.lab.dolphin.server.DaoManager;
import android.R.integer;
import android.content.Context;
import android.util.Log;

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
				double[] tweakedData = DaoManager.toDoubleArray(rawGestureDatas.get(j).getData());
				Feature[] nodes = new FeatureNode[tweakedData.length];

				for (int k = 0; k < tweakedData.length; k++) {
					nodes[k] = new FeatureNode(k+1,tweakedData[k]);
				}
				features.add(nodes);
			}
		}
		
		Parameter parameter = new Parameter(SolverType.L2R_L2LOSS_SVC_DUAL, 2.0, 0.01);
		
		Problem problemA = new Problem();
		problemA.l = lables.size();
		problemA.n = 2*CoreSettings.data_time*CoreSettings.data_width;
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

	
	public static void train_raw_data(InputStream is) throws IOException, ClassNotFoundException{
				
		ObjectInputStream ois = new ObjectInputStream(is);
		List<RichFeatureData> datas = (List<RichFeatureData>) ois.readObject();
		
		List<Double> lablesForNF = new LinkedList<Double>();
		List<Feature[]> featuresForNF = new LinkedList<Feature[]>();
		
		List<Double> lablesForFN = new LinkedList<Double>();
		List<Feature[]> featuresForFN = new LinkedList<Feature[]>();
		
		List<Double> lablesForNFNF = new LinkedList<Double>();
		List<Feature[]> featuresForNFNF = new LinkedList<Feature[]>();
		
		List<Double> lablesForCR = new LinkedList<Double>();
		List<Feature[]> featuresForCR = new LinkedList<Feature[]>();
		
		for (int i = 0; i < datas.size(); i++) {
			RichFeatureData richFeatureData = datas.get(i);
			Feature[] nodes = new FeatureNode[richFeatureData.data.length];

			for (int i1 = 0; i1 < nodes.length; i1++) {
				nodes[i1] = new FeatureNode(i1+1,richFeatureData.data[i1]);//TODO 0?
			}
			
			Double lable;
			switch (richFeatureData.feature_id) {
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				lable = (double) richFeatureData.feature_id - 1;
				lablesForNF.add(lable);
				featuresForNF.add(nodes);
				break;
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
				lable = (double) richFeatureData.feature_id - 6;
				lablesForFN.add(lable);
				featuresForFN.add(nodes);
				break;
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				lable = (double) richFeatureData.feature_id - 11;
				lablesForNFNF.add(lable);
				featuresForNFNF.add(nodes);
				break;
			case 16:
			case 17:
				lable = (double) richFeatureData.feature_id - 16;
				lablesForCR.add(lable);
				featuresForCR.add(nodes);
				break;

			default:
				break;
			}
		}
		
		createModel("/storage/sdcard0/dolphin_home/models/nf_default.dolphin", lablesForNF, featuresForNF);
		createModel("/storage/sdcard0/dolphin_home/models/fn_default.dolphin", lablesForFN, featuresForFN);
		createModel("/storage/sdcard0/dolphin_home/models/nfnf_default.dolphin", lablesForNFNF, featuresForNFNF);
		createModel("/storage/sdcard0/dolphin_home/models/cr_default.dolphin", lablesForCR, featuresForCR);
		
		ois.close();
		is.close();
	}
	
	static void createModel(String path, List<Double> lables, List<Feature[]> features){
		
		Parameter parameter = new Parameter(SolverType.L2R_L2LOSS_SVC_DUAL, 2.0, 0.01);
		
		Problem problemA = new Problem();
		problemA.l = lables.size();
		problemA.n = 2*CoreSettings.data_time*CoreSettings.data_width;
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
		
		try {
			modelA.save(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
