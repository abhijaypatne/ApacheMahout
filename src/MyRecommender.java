import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;


public class MyRecommender {

	private DataModel model;
	private UserSimilarity similarity;
	private UserNeighborhood neighborhood;
	private UserBasedRecommender recommender;
	
	public MyRecommender(String filePath){
		try{
			model = new FileDataModel(new File(filePath));
			similarity = new PearsonCorrelationSimilarity(model);
			neighborhood = new ThresholdUserNeighborhood(0.01, similarity, model);
			recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
		}
		catch(IOException io){
			System.out.println("File IO exception: "); 
			io.printStackTrace();
		}catch (TasteException te) {
			System.out.println("TasteException occured: ");
			te.printStackTrace();
		}catch(Exception e){
			System.out.println("Exception: "); 
			e.printStackTrace();			
		}
	}
	
	public List<RecommendedItem> getRecommendations(int userId){
		List<RecommendedItem> recommendations = null;
		try {
			recommendations = this.recommender.recommend(userId, 10);
		} catch (TasteException e) {
			System.out.println("TasteException occured: ");
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("Exception: "); 
			e.printStackTrace();			
		}
		
		return recommendations;
	}
	
	public static void main(String[] args){
		int userId = 666;
		MyRecommender mr = new MyRecommender("/home/cloudera/Downloads/hw5/recommender/ml-100k/output.csv");
		List<RecommendedItem> recommendations = mr.getRecommendations(userId);
		
		System.out.println("Recommended items: ");
		for (RecommendedItem recommendation:recommendations){
			System.out.println(recommendation.getItemID());
		}
	}

}
