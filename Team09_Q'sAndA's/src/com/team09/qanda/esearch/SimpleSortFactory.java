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

	public SimpleSortFactory(String category){
		this.category=category;
	}
	public Sort createSort(){
		String sort ="";
		Sorting direction=Sorting.DESC;
		if(category.equals(HasPictures)){
			sort="hasPictures";
		}
		else if(category.equals(MostUpvotes) || category.equals(LeastUpvotes)){
			sort="upVotes";
			if(category.equals(LeastUpvotes)){
				direction=Sorting.ASC;
			}
		}
		else if(category.equals(MostRecent) || category.equals(Oldest)){
			sort="relativeDate";
			if(category.equals(MostRecent)){
				direction=Sorting.ASC;
			}
		}
		return new Sort(sort, direction);
	}
}
