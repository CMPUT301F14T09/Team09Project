package com.team09.qanda.esearch;

import io.searchbox.core.search.sort.Sort;
import io.searchbox.core.search.sort.Sort.Sorting;

public class SimpleSortFactory {
	public static final String HasPictures = "Has Picture";
	public static final String MostUpvotes = "Most Upvoted";
	public static final String LeastUpvotes = "Least Upvoted";
	public static final String Oldest = "Oldest";
	public static final String MostRecent = "Newest";
	private String category;
	/**
	 * SimpleSortFactory creates a Jest Sort object 
	 * (from io.searchbox.core.search.sort.Sort) to be used
	 * sort queries from an ElasticSearch server. The category (ie. the field to sort by)
	 * should be one SimpleSort.HasPictures,SimpleSort.MostUpvotes, SimpleSort.LeastUpvotes,
	 * SimpleSort.Oldest, or SimpleSort.MostRecent. SimpleSortFactory is intended
	 * to be used for sorting QuestionThreads only
	 * @param category, the field to sort the QuestionThreads by
	 */
	public SimpleSortFactory(String category){
		this.category=category;
	}
	/**
	 * Create the sort object to be used by Jest or the ElasticSearch handler
	 * @return the Sort object to be used to sort queries on the ElasticSearch server
	 */
	public Sort createSort(){
		String sort ="";
		Sorting direction=Sorting.DESC;
		if(category.equals(HasPictures)){
			sort="hasPictures";
		}
		else if(category.equals(MostUpvotes) || category.equals(LeastUpvotes)){
			sort="question.upVotes";
			if(category.equals(LeastUpvotes)){
				direction=Sorting.ASC;
			}
		}
		else if(category.equals(MostRecent) || category.equals(Oldest)){
			sort="question.relativeDate";
			if(category.equals(Oldest)){
				direction=Sorting.ASC;
			}
		}
		return new Sort(sort, direction);
	}
}
