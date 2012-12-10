package dk.itu.biologger;

import java.io.Serializable;

import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.neural.NeuralConnection;

public class ArousalPredictor implements Serializable {

	private static final long serialVersionUID = 6311023356789484185L;
	
	String[] options = {"-L", "0.3", "-M", "0.0", "-N", "20000", "-V", "0", "-S", "0", "-E", "20", "-H", "7,7","-R"};
	MarioMultiLayerPerceptron mmlp;
	
	DataSource baseDataSource;
	Instances baseInstances;
	
	DataSource personalDataSource;
	Instances personalInstances;
	
	public boolean init(){
		try {
			mmlp = new MarioMultiLayerPerceptron();
			mmlp.setOptions(options);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void initialTraining()
	{
		try{
			loadData("baseData.arff");
			mmlp.buildClassifier(baseInstances);
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void loadWeights()
	{
		NeuralConnection[] conns = mmlp.getNeuralConnections();
		for(int i = 0; i < conns.length; i++)
		{
			conns[i].weightValue(i);
		}
	}
	
	public void loadData(String baseDataFile){
		try{
			baseDataSource = new DataSource(baseDataFile);
			baseInstances = baseDataSource.getDataSet();
			baseInstances.setClassIndex(baseInstances.numAttributes()-1);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void trainPersonal(String personalDataFile)
	{
		try{
			personalDataSource = new DataSource(personalDataFile);
			personalInstances = personalDataSource.getDataSet();
			personalInstances.setClassIndex(personalInstances.numAttributes()-1);
			mmlp.buildClassifier(personalInstances);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
