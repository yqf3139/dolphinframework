package seu.lab.dolphin.learn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;
import seu.lab.dolphin.client.GestureData.RichFeatureData;
import seu.lab.dolphin.dao.Gesture;
import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;

public class LinearTrainner {
	
	public static void train(String modelPath, Gesture[] gestures) throws IOException {
		List<Double> lables = new LinkedList<Double>();
		List<Feature[]> features = new LinkedList<Feature[]>();
		
		for (int i = 1; i <= 12; i++) {
			if(i == 7) continue;
			try {
				FileInputStream fis = new FileInputStream("/home/yqf/Desktop/gesture/gesture_data_"+i);
				ObjectInputStream ois = new ObjectInputStream(fis);
				List<RichFeatureData> list = (List<RichFeatureData>) ois.readObject();
				for (int j = 0; j < list.size(); j++) {
					RichFeatureData portableData = list.get(j);
					lables.add((double) (i/2));
					double[] shadow = new double[60];

					Feature[] nodes = new FeatureNode[shadow.length];

					for (int k2 = 0; k2 < shadow.length; k2++) {
						shadow[k2] = 0;
						for (int l = 0; l < 50; l++) {
							//shadow[k2] = Math.max(shadow[k2], portableData.data[l*60+k2]);
							shadow[k2] += portableData.data[l*60+k2];
						}
						if(shadow[k2] < 0.00001)shadow[k2] = 0;
					}
					
					for (int k = 0; k < shadow.length; k++) {
						nodes[k] = new FeatureNode(k+1,shadow[k]);
					}
					
					features.add(nodes);
					System.out.println("adding"+(double) (i/2)+" => ");
				}

				fis.close();
				ois.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Parameter parameter = new Parameter(SolverType.L2R_L2LOSS_SVC_DUAL, 1.0, 0.01);
		
		Problem problemA = new Problem();
		problemA.l = lables.size();
		problemA.n = 60;
		problemA.y = new double[lables.size()];
		problemA.x = new Feature[lables.size()][];
		problemA.bias = 1.0;
		
		for (int i1 = 0,j = 0; i1 < lables.size(); i1++) {
			problemA.y[i1] = lables.get(i1);
//			if(problemA.y[i1] == 2) problemA.y[i1] = 1;
			problemA.x[i1] = features.get(i1);
		}
		
		Model modelA = Linear.train(problemA, parameter);

		for (int i1 = 0; i1 < features.size(); i1++) {
			double prediction = Linear.predict(modelA, features.get(i1));
			System.out.println(lables.get(i1)+" -> "+prediction);

		}
		
		modelA.save(new File("/home/yqf/Desktop/nf_linear_simple.dolphin"));

	}
}
