package model;

import java.sql.Connection;
import java.util.ArrayList;

import dao.Database;
//import dao.Project;
import dto.CategoryFeed;
import dao.ProjectFeeds;

public class FeedManager {
	public ArrayList<CategoryFeed> GetCategories() throws Exception {
		ArrayList<CategoryFeed> feeds = null;
		try {
			Database database= new Database();
			Connection connection = database.getConnection();
			ProjectFeeds project= new ProjectFeeds();
			feeds=project.GetCategories(connection);
		}
		catch (Exception e) {
			throw e;
		}
		return feeds;
	}
}
