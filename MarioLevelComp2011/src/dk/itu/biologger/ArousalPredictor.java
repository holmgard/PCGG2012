package dk.itu.biologger;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import dk.itu.mario.level.generator.bio.Chunk;
import dk.itu.mario.level.generator.bio.ScreenChunk;
import dk.itu.mario.level.generator.bio.ScreenChunkLibrary;

import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.neural.NeuralConnection;

public class ArousalPredictor implements Serializable {

	private static final long serialVersionUID = 6311023356789484185L;
	
	String[] options = {"-L", "0.3", "-M", "0.0", "-N", "1000", "-V", "0", "-S", "0", "-E", "20", "-H", "3,2","-R"};
	MultilayerPerceptron mmlp;
	
	DataSource baseDataSource;
	Instances baseInstances;
	
	DataSource personalDataSource;
	Instances personalInstances;
	
	String singlePlayerName = "noname";
	
	public static void main(String[] bleh)
	{
		ArousalPredictor pred = new ArousalPredictor();
		pred.init("generalTreatedPseudoAmplitude.arff");
	}
	
	public boolean init(String singlePlayerDataset){
		singlePlayerName = singlePlayerDataset;
		System.out.println("Loading data: " + singlePlayerName);
		try {
			mmlp = new MultilayerPerceptron();
			mmlp.setOptions(options);
			loadData(singlePlayerDataset);
			mmlp.buildClassifier(baseInstances);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void SavePersonalScreenChunkLibrary()
	{
		ScreenChunkLibrary scl = ScreenChunkLibrary.getInstance();
		int scRemaining = scl.getNumOfChunks();
		int scId = 0;
		
		while(scRemaining > 0)
		{
			ScreenChunk sc = scl.getChunk(scId);
			if(sc != null)
			{
				List<Chunk> chunks = sc.getChunks(null);
			
				int[] chunkCounts = new int[scl.getNumOfChunks()];
				for(Chunk chunk : chunks){
					//chunkCounts[chunk.getId()] += 1;
					chunkCounts[scId] += 1;
				}
			
			
				//System.out.println("ChuknCounts: " + chunkCounts.length);
				Instance scInstance = new Instance(chunkCounts.length + 2);
				//System.out.println("Base instances:");
				//System.out.println(baseInstances.toString());
				scInstance.setDataset(baseInstances);
				System.out.println("Current instance: ");
				System.out.println(scInstance.toString());
				for(int chunk = 0; chunk < chunkCounts.length; chunk++)
				{
					//System.out.println("chunk" + chunk);
					//Attribute att = new Attribute("chunk" + chunk);
					//System.out.println("Created attribute: " + att.name());
					scInstance.setValue(scInstance.attribute(chunk), (double)chunkCounts[chunk]);
				}
				scInstance.setValue(scInstance.attribute(chunkCounts.length), 0.0d);
				scInstance.setValue(scInstance.attribute(chunkCounts.length+1), 0.0d);
				System.out.println("Current instance: ");
				System.out.println(scInstance.toString());
				try {
					double prediction = mmlp.classifyInstance(scInstance);
					sc.setValue((float)prediction);
					System.out.println("Predicted value: " + prediction);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				scRemaining--;
			}
			scId++;
			
		}
		
		
		try {
			scl.writeLibToFile(new File(singlePlayerName + ".res"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
/*	public void initialTraining()
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
	}*/
	
	public void loadData(String baseDataFile){
		try{
			baseDataSource = new DataSource(baseDataFile);
			baseInstances = baseDataSource.getDataSet();
			//baseInstances.deleteAttributeAt(42);
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
