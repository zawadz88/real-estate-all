package com.zawadz88.realestate.service;

import com.zawadz88.realestate.model.Article;
import com.zawadz88.realestate.model.ArticleEssential;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class holding all the fetched content in the application
 * Created: 22.01.14
 *
 * @author Piotr Zawadzki
 */
public class ContentHolder {

	/**
	 * Map of article essential lists grouped by {@link com.zawadz88.realestate.model.ArticleCategory} names
	 */
	private Map<String, List<ArticleEssential>> mArticleEssentialListsByCategory = new HashMap<String, List<ArticleEssential>>();

	/**
	 * Map o {@link com.zawadz88.realestate.model.Article}s grouped by article IDs
	 */
	private Map<Long, Article> mArticlesByIdMap = new HashMap<Long, Article>();

	/**
	 * Map containing numbers of lastly fetched article essential pages for {@link com.zawadz88.realestate.model.ArticleCategory} name
	 */
	private Map<String, Integer> mArticleEssentialCurrentPageNumbersByCategory = new HashMap<String, Integer>();

	/**
	 * Map containing <i>loading more</i> flags for {@link com.zawadz88.realestate.model.ArticleCategory} names.
	 * A <i>loading more</i> flag is set to {@code true} if the app is loading a next page of articles for a given category.
	 */
	private Map<String, Boolean> mArticleEssentialLoadingMoreFlagByCategory = new HashMap<String, Boolean>();

	/**
	 * Map containing <i>end of items reached</i> flags for {@link com.zawadz88.realestate.model.ArticleCategory} names.
	 * A <i>end of items reached</i> flag is set to {@code true} if there is no more items to be fetched for a given category
	 */
	private Map<String, Boolean> mArticleEssentialEndOfItemsReachedFlagByCategory = new HashMap<String, Boolean>();

	/**
	 * Get a list of {@link com.zawadz88.realestate.model.ArticleEssential}s for a given category name
	 * @param categoryName name of the category
	 * @return
	 */
	public synchronized List<ArticleEssential> getArticleEssentialListByCategory(final String categoryName) {
		List<ArticleEssential> articleEssentialList = mArticleEssentialListsByCategory.get(categoryName);
		if (articleEssentialList == null) {
			articleEssentialList = new ArrayList<ArticleEssential>();
			mArticleEssentialListsByCategory.put(categoryName, articleEssentialList);
		}
		return articleEssentialList;
	}

	/**
	 *
	 * @param categoryName
	 * @return
	 */
	public synchronized int getCurrentlyLoadedPageNumberForCategory(final String categoryName) {
		int result = -1;
		Integer currentNumber = mArticleEssentialCurrentPageNumbersByCategory.get(categoryName);
		if (currentNumber != null) {
			result = currentNumber;
		}
		return result;
	}

	/**
	 *
	 * @param categoryName
	 * @param pageNumber
	 */
	public synchronized void setCurrentlyLoadedPageNumberForCategory(final String categoryName, final int pageNumber) {
		mArticleEssentialCurrentPageNumbersByCategory.put(categoryName, pageNumber);
	}

	/**
	 *
	 * @param categoryName
	 * @return
	 */
	public synchronized boolean getLoadingMoreFlagForCategory(final String categoryName) {
		boolean result = false;
		Boolean loadingMoreItems = mArticleEssentialLoadingMoreFlagByCategory.get(categoryName);
		if (loadingMoreItems != null) {
			result = loadingMoreItems;
		}
		return result;
	}

	/**
	 *
	 * @param loadingMoreItems
	 * @param categoryName
	 */
	public synchronized  void setLoadingMoreFlagForCategory(final boolean loadingMoreItems, final String categoryName) {
		mArticleEssentialLoadingMoreFlagByCategory.put(categoryName, loadingMoreItems);
	}

	/**
	 *
	 * @param categoryName
	 * @return
	 */
	public synchronized boolean getEndOfItemsReachedFlagForCategory(final String categoryName) {
		boolean result = false;
		Boolean endOfItemsReached = mArticleEssentialEndOfItemsReachedFlagByCategory.get(categoryName);
		if (endOfItemsReached != null) {
			result = endOfItemsReached;
		}
		return result;
	}

	/**
	 *
	 * @param endOfItemsReached
	 * @param categoryName
	 */
	public synchronized  void setEndOfItemsReachedFlagForCategory(final boolean endOfItemsReached, final String categoryName) {
		mArticleEssentialEndOfItemsReachedFlagByCategory.put(categoryName, endOfItemsReached);
	}

	/**
	 *
	 * @param articleId
	 * @return
	 */
	public synchronized Article getArticleById(final long articleId) {
		return mArticlesByIdMap.get(articleId);
	}

	public synchronized void addArticle(final Article article) {
		mArticlesByIdMap.put(article.getArticleId(), article);
	}
}
