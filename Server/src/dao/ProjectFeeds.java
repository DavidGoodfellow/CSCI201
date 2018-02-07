package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import dto.CategoryFeed;

public class ProjectFeeds {
	public ArrayList<CategoryFeed> GetCategories(Connection connection) throws Exception
	{
		ArrayList<CategoryFeed> feedData = new ArrayList<CategoryFeed>();
		try
		{
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM Categories");
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				CategoryFeed feedObject = new CategoryFeed();
				/*feedObject.setTitle(rs.getString("title"));
				feedObject.setDescription(rs.getString("description"));
				feedObject.setUrl(rs.getString("url"));
				feedData.add(feedObject);*/
			}
			return feedData;
		}
		catch(Exception e)
		{
			throw e;
		}
		}
}
