package tagcloud;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;

public class Top20TagCloud {
	private static ArrayList<String> getTop20IDs(String fileName) {
		ArrayList<String> IDs = new ArrayList<String>();
		
		BufferedReader inputStream = null;
		
		try {
			inputStream = new BufferedReader(new FileReader(fileName));
			
			String l;
			while((l = inputStream.readLine()) != null) {
				IDs.add(l);
			}
		} catch (IOException e) {
			System.err.println("IOException: "+e.getMessage());
			System.exit(1);
		}
		
		return IDs;
	}
	
	private static void getTweets(ArrayList<String> IDs) throws InterruptedException {
		for (int i = 0; i < IDs.size(); i++) {
			String userName = IDs.get(i);
			TwitterCrawler.crawlingMessage(userName);
		}
	}
	
	private static void getTagcloud(String textFile) throws IOException {
		final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
		final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load("./output/"+textFile+".txt");
		final Dimension dimension = new Dimension(600, 600);
		final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
		wordCloud.setPadding(2);
		wordCloud.setBackground(new RectangleBackground(dimension));
		wordCloud.setBackgroundColor(Color.WHITE);
		wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
		wordCloud.setFontScalar(new LinearFontScalar(20, 60));
		wordCloud.build(wordFrequencies);
		wordCloud.writeToFile("./output/"+textFile+".png");
	}
	
	private static void drawTagCloud(ArrayList<String> IDs) throws IOException {
		for (int i = 0; i < IDs.size(); i++) {
			String textFile = IDs.get(i);
			getTagcloud(textFile);
		}
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		ArrayList<String> IDs = getTop20IDs("top20ids.txt");
		getTweets(IDs);
		drawTagCloud(IDs);
	}
}
