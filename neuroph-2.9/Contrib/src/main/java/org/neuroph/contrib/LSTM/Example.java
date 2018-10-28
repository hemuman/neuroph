/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.LSTM;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.TransferFunctionType;

public class Example implements LearningEventListener  {
 @Override
 public void handleLearningEvent(LearningEvent event) {
		SupervisedLearning rule = (SupervisedLearning)event.getSource();
		System.out.println( "Training, Network Epoch " + rule.getCurrentIteration() + ", Error:" + rule.getTotalNetworkError());
    }
    public static NeuralNetwork assembleNeuralNetwork() {

        Layer inputLayer = new Layer();
        inputLayer.addNeuron(new BiasNeuron());
        inputLayer.addNeuron(new BiasNeuron());

        Layer hiddenLayerOne = new Layer();
        hiddenLayerOne.addNeuron(new BiasNeuron());
        hiddenLayerOne.addNeuron(new BiasNeuron());
        hiddenLayerOne.addNeuron(new BiasNeuron());
        hiddenLayerOne.addNeuron(new BiasNeuron());

        Layer hiddenLayerTwo = new Layer();
        hiddenLayerTwo.addNeuron(new BiasNeuron());
        hiddenLayerTwo.addNeuron(new BiasNeuron());
        hiddenLayerTwo.addNeuron(new BiasNeuron());
        hiddenLayerTwo.addNeuron(new BiasNeuron());

        Layer outputLayer = new Layer();
        outputLayer.addNeuron(new BiasNeuron());

        NeuralNetwork ann = new NeuralNetwork();

        ann.addLayer(0, inputLayer);
        ann.addLayer(1, hiddenLayerOne);
        ConnectionFactory.fullConnect(ann.getLayerAt(0), ann.getLayerAt(1));
        ann.addLayer(2, hiddenLayerTwo);
        ConnectionFactory.fullConnect(ann.getLayerAt(1), ann.getLayerAt(2));
        ann.addLayer(3, outputLayer);
        ConnectionFactory.fullConnect(ann.getLayerAt(2), ann.getLayerAt(3));
        ConnectionFactory.fullConnect(ann.getLayerAt(0), ann.getLayerAt(ann.getLayersCount() - 1), false);

        ann.setInputNeurons(inputLayer.getNeurons());
        ann.setOutputNeurons(outputLayer.getNeurons());

        ann.setNetworkType(NeuralNetworkType.MULTI_LAYER_PERCEPTRON);
        return ann;
    }

    public static NeuralNetwork trainNeuralNetwork(NeuralNetwork ann) {
        int inputSize = 2;
        int outputSize = 1;
        DataSet ds = new DataSet(inputSize, outputSize);

        DataSetRow rOne = new DataSetRow(new double[] { 0, 1 }, new double[] { 1 });
        ds.addRow(rOne);
        DataSetRow rTwo = new DataSetRow(new double[] { 1, 1 }, new double[] { 0 });
        ds.addRow(rTwo);
        DataSetRow rThree = new DataSetRow(new double[] { 0, 0 }, new double[] { 0 });
        ds.addRow(rThree);
        DataSetRow rFour = new DataSetRow(new double[] { 1, 0 }, new double[] { 1 });
        ds.addRow(rFour);

        BackPropagation backPropagation = new BackPropagation();
        backPropagation.setMaxIterations(10000);

        ann.learn(ds, backPropagation);
        return ann;
    }
    
    public static void main(String... args) throws FileNotFoundException, IOException, ClassNotFoundException {
      System.out.println("Time stamp N1:" + new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:MM").format(new Date()));
      String dirDefault="G:/bkp/$AVG/baseDir/Imports/Sprites/txts/";
    List<String> fileNamesList = new ArrayList();
      Files.newDirectoryStream(Paths.get(dirDefault),
      path -> path.toString().endsWith(".txt")).forEach(filePath -> fileNamesList.add(filePath.toString()));
      StringBuilder stringBuilder = new StringBuilder();
     for(String filepath:fileNamesList)
     {  BufferedReader reader = new BufferedReader(new FileReader(args.length<1?filepath:args[0]));
        char[] buffer = new char[10];
        while (reader.read(buffer) != -1) {
                stringBuilder.append(new String(buffer));
                buffer = new char[10];
        }
        reader.close();
    }
        String exampleText = stringBuilder.toString();//.replaceAll("[^a-zA-Z0-9\\s+]", "");;

      
      
//       exampleText="Ek kone mein tha baitha\n" +
//                            "Jab mujhe woh dikhi thi\n" +
//                            "Woh sab se alag thi\n" +
//                            "Woh sab se haseen\n" +
//                            "\n" +
//                            "Ankhen meri phir uski aankho se ja mili\n" +
//                            "Ik wari do wari phir nikli hasi\n" +
//                            "Ik hath hawa vich duje ch glass\n" +
//                            "Te mainu ni pata si glass ch ki\n" +
//                            "But hauli hauli lak nu ghumake nachdi\n" +
//                            "Lage so sexy\n" +
//                            "\n" +
//                            "O takdi mainu main takda ohnu\n" +
//                            "Mahaul mein tension badhne lagi\n" +
//                            "Khuda ki kasam na kabhi maine pi\n" +
//                            "Haan phir bhi thodi thodi chadne lagi\n" +
//                            "\n" +
//                            "Ruka na gaya main gaya ohde kol\n" +
//                            "I said I wanna talk\n" +
//                            "Oh kehndi mainu bol\n" +
//                            "Main ohde nehde jaa ke ohnu pucheya\n" +
//                            "Baby do you freak like you dance?\n" +
//                            "'Cause you dance like a pro\n" +
//                            "\n" +
//                            "Have Mercy on me\n" +
//                            "Have Mercy on me\n" +
//                            "\n" +
//                            "Jaise hilti hai waise math hila kar\n" +
//                            "Laghe mujhe rahegi Rabb se mila kar\n" +
//                            "Body teri hotter than Chinchilla fur\n" +
//                            "Teri maa ne tujhe bada kiya kya khila kar\n" +
//                            "\n" +
//                            "Ruk jaaye dil when you get down low\n" +
//                            "Karlo reham thoda meri maan lo\n" +
//                            "Aise meri jaan na meri jaan lo\n" +
//                            "Aise meri jaan na meri jaan lo\n" +
//                            "Mercy Lyrics at Lyricsted.com\n" +
//                            "\n" +
//                            "Dhoonda but koi na nikli kami\n" +
//                            "Pairon ke niche se nikli zameen\n" +
//                            "Band bhi karo ab karna yoon tease\n" +
//                            "Hathon ko jod ke kehta hoon please\n" +
//                            "\n" +
//                            "Have mercy on me\n" +
//                            "Have mercy on me\n" +
//                            "\n" +
//                            "Karlo reham thoda karlo reham (x7)\n" +
//                            "\n" +
//                            "Karlo reham thoda karlo..\n" +
//                            "\n" +
//                            "It's your boy Badshah!\n" +
//                            "\n Club mein tu aayi\n" +
//"Hone ko high\n" +
//"Head to toe coco mein nahayi\n" +
//"\n" +
//"Seedha gayi bar pe\n" +
//"Deti nahi fuk\n" +
//"Lagta hai taaza taaza hua breakup\n" +
//"Baby all alone na koi sang\n" +
//"Hirni si aankhein baby sanwala rang\n" +
//"Dekh ke tujhko phooli hain saansein meri\n" +
//"Akad gaye mere ang\n" +
//"\n" +
//"Baby kuch bhi raha nahi bass mein\n" +
//"Aise jaan na le meri hans ke\n" +
//"Le baby neat pakad, beat pakad\n" +
//"Zor lagake thoda kass ke\n" +
//"\n" +
//"Ni main Vodka laga ke tere naal nachna\n" +
//"Ni main Vodka laga ke tere naal nachna\n" +
//"Hoy main tera hi naa’ ratna\n" +
//"Addi maar ke vehda pattna\n" +
//"Ajj main talli hoke hattna ni (x2)\n" +
//"\n" +
//"Ho baby baby you don’t care\n" +
//"Hawa mein tere udte hair\n" +
//"Haath donon in the air\n" +
//"Tu lage mujhe zone mein\n" +
//"\n" +
//"Ho baby baby understand\n" +
//"De apna tu instagram\n" +
//"Ya le le mera number tu\n" +
//"Save kar phone mein\n" +
//"\n" +
//"Fail karde tu nashe charas ke\n" +
//"Tere nakhre na kisi ke bass ke\n" +
//"Le baby neat pakad, beat pakad\n" +
//"Zor lagake thoda kass ke\n" +
//"\n" +
//"Ni main Vodka laga ke tere naal nachna\n" +
//"Ni main Vodka laga ke tere naal nachna\n" +
//"Hoy main tera hi naa’ ratna\n" +
//"Addi maar ke vehda pattna\n" +
//"Ajj main talli hoke hattna ni (x2)\n" +
//"\n" +
//"It’s yo boy Badshah!\n" +
//"\n" +
//"Ho baby baby you don’t care\n" +
//"Hawa mein tere udte hair\n" +
//"Haath donon in the air\n" +
//"Tu lage mujhe zone mein\n" +
//"\n"  ;
      HashMap encoding=TextEncoder.getEncoding(exampleText.replaceAll("\n", "").replaceAll(",", "").replace(".", " .").replaceAll(",", "").split(" "));
      int inputNeurons=3;
      int outputNeurons=1;
      DataSet trainingSet= TextEncoder.getDataSet(exampleText,encoding, inputNeurons,outputNeurons);
      
        int maxIterations = 10;
        NeuralNetwork neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,inputNeurons,2*inputNeurons+1, outputNeurons);
        ((LMS) neuralNet.getLearningRule()).setMaxError(0.01);//0-1
        ((LMS) neuralNet.getLearningRule()).setLearningRate(0.2);//0-1
        ((LMS) neuralNet.getLearningRule()).setMaxIterations(maxIterations);//0-1
        BackPropagation learningRule = (BackPropagation) neuralNet.getLearningRule();

        learningRule.setLearningRate(0.2);
 
        learningRule.setMaxError(0.001);
        learningRule.setMaxIterations(maxIterations);
        //add learning listener in order to print out training info
        learningRule.addListener((LearningEvent event) -> {
            BackPropagation bp = (BackPropagation) event.getSource();
            if (event.getEventType().equals(LearningEvent.Type.LEARNING_STOPPED)) {
                System.out.println();
                System.out.println("Training completed in " + bp.getCurrentIteration() + " iterations");
                System.out.println("With total error " + bp.getTotalNetworkError() + '\n');
            } else {
                System.out.println("Iteration: " + bp.getCurrentIteration() + " | Network error: " + bp.getTotalNetworkError());
            }
      });
        
        if(!new File(dirDefault+inputNeurons+"temp.nnet").exists())//Learn if not done already
        { 
            neuralNet.learn(trainingSet);
            ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(dirDefault+inputNeurons+"map.txt"));
            obj.writeObject(encoding);
            obj.close();
        
            neuralNet.save(dirDefault+inputNeurons+"temp.nnet");
        }else{
            ObjectInputStream obj = new ObjectInputStream(new FileInputStream(dirDefault+inputNeurons+"map.txt"));
            encoding= (HashMap<String, Double>)obj.readObject();
            neuralNet= NeuralNetwork.createFromFile(dirDefault+inputNeurons+"temp.nnet");
        }
        
        
        System.out.println("Time stamp N2:" + new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:MM").format(new Date())
                +"\t Iterarions="+((LMS) neuralNet.getLearningRule()).getCurrentIteration()
        +"\t Error ="+((LMS) neuralNet.getLearningRule()).getTotalNetworkError());
        
        String exampleTestText="how could that which I spoke so many years ago";//exampleText.substring(1000, 1000+inputNeurons+3);//
        
        
       // for (DataSetRow testDataRow : testSet.getRows()) {
       double temp=0;
       for(int i=0;i<1000;i++)
       {   DataSet testSet= TextEncoder.getDataSet(exampleTestText,encoding, inputNeurons,outputNeurons);
            HashMap decoding=TextEncoder.getDecoding(encoding); 
           DataSetRow testDataRow = testSet.getRowAt(testSet.size()-1);
            neuralNet.setInput(testDataRow.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();
            System.out.print(" Input: " + Arrays.toString(testDataRow.getInput()) );
            System.out.println(" Output: " + Arrays.toString(networkOutput)) ;
            
            
            for(double textCode:testDataRow.getInput()){
                 //System.out.print(decoding.get(TextEncoder.round(textCode,encoding.size()/5))+" " );
      
            }
            
            for(double textCode:networkOutput){
                if(textCode==0 | temp==textCode) textCode= TextEncoder.round(Math.random(),encoding.size()/10);
                temp=textCode;
                 //System.out.print(TextEncoder.findClosestMatch(decoding, TextEncoder.round(textCode,encoding.size()/5)) ); // decoding.get(TextEncoder.round(textCode,encoding.size()/5))+" " 
                 exampleTestText=exampleTestText +" "+TextEncoder.findClosestMatch(decoding, TextEncoder.round(textCode,encoding.size()/10));
            }
            
            if(i==0){
                exampleTestText=exampleTestText.replaceAll("bla bla", "");
            }
           // System.out.println("\n"+exampleTestText);
            
    }
           // System.out.print(TextEncoder.round(textCode,encoding.size()/10-10)+" OutputText: " +decoding.get(TextEncoder.round(textCode,encoding.size()/10-10)) );
     //  }
        
        System.out.println("\n"+exampleTestText);
    }
}