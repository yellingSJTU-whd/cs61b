package lab14;

import lab14lib.*;

import java.util.ArrayList;
import java.util.random.RandomGeneratorFactory;

public class Main {
	public static void main(String[] args) {
		/** Your code here. */
//		Generator generator = new StrangeBitwiseGenerator(512);
//		GeneratorDrawer gd = new GeneratorDrawer(generator);
//		gd.draw(4096);

		Generator generator = new StrangeBitwiseGenerator(1024);
		GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
		gav.drawAndPlay(128000, 1000000);
	}
} 