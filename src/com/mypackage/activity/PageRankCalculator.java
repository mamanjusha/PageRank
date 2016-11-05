package com.mypackage.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mypackage.model.Page;
import com.mypackage.util.Constants;

/**
 * Purpose of this class is to calculate the page rank for given pages.
 * 
 * @author Manjusha
 *
 */
public final class PageRankCalculator {

	private static final int DEFAULT_NUMBER_ITERATIONS = 10;

	private final int numberOfIterations;

	private final String inputFilePath;

	public PageRankCalculator(final int numberOfIterations, final String inputFilePath) {
		super();
		this.numberOfIterations = numberOfIterations;
		this.inputFilePath = inputFilePath;
	}

	public static void main(final String[] args) throws IOException {
		String inputFilePath = null;
		int numberOfIterations;
		if (args == null || args.length == 0) {
			System.err.println("Input arguments are null/empty");
			return;
		}
		if (args[0] == null || args[0].isEmpty()) {
			System.err.println("FileName is null/empty");
			return;
		}
		inputFilePath = args[0];
		if (args[1] == null || args[1].isEmpty()) {
			System.out.println(
					"Input number of iterations are null/empty. Using default iteration " + DEFAULT_NUMBER_ITERATIONS);
			numberOfIterations = DEFAULT_NUMBER_ITERATIONS;
		} else {
			numberOfIterations = Integer.parseInt(args[1]);
		}
		final PageRankCalculator pageRankCalculator = new PageRankCalculator(numberOfIterations, inputFilePath);
		final List<Page> pages = pageRankCalculator.scanPages();
		pageRankCalculator.evaluatePageRank(pages);

	}

	private void evaluatePageRank(final List<Page> pages) {
		final Map<String, Double> pageIdToRankMap = new HashMap<String, Double>();
		final Map<String, Page> pageIdToPageMap = new HashMap<String, Page>();

		for (Page page : pages) {
			pageIdToRankMap.put(page.getPageId(), page.getRank());
			pageIdToPageMap.put(page.getPageId(), page);
		}
		for (int count = 0; count < numberOfIterations; count++) {
			for (Page page : pages) {
				double pageRank = 0;
				for (String pageId : page.getIncomingPageIds()) {
					final Page incomingPage = pageIdToPageMap.get(pageId);
					pageRank += (incomingPage.getRank() / incomingPage.getOutGoingPageIds().size());
				}
				pageRank = (1 - Constants.DAMPING_FACTOR) / pages.size() + pageRank * Constants.DAMPING_FACTOR;
				pageIdToRankMap.put(page.getPageId(), pageRank);
			}
		}

		for (Map.Entry<String, Double> entry : pageIdToRankMap.entrySet()) {
			pageIdToPageMap.get(entry.getKey()).setRank(entry.getValue());
		}
		System.out.println(pages);
	}

	/**
	 * Scans pages from input file.
	 * 
	 * @return List<Page>
	 * @throws IOException
	 */
	private List<Page> scanPages() throws IOException {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(new File(inputFilePath)));

			final String numberOfPagesString = bufferedReader.readLine();
			if (numberOfPagesString == null || numberOfPagesString.isEmpty()) {
				throw new RuntimeException("Number of pages input is null/empty");
			}
			final int numberOfPages = Integer.parseInt(numberOfPagesString);
			final String[][] inputMatrix = new String[numberOfPages][numberOfPages];
			final List<Page> pages = new ArrayList<Page>();
			
			for (int count = 0; count < numberOfPages; count++) {

				final String outgoingLinks = bufferedReader.readLine();
				if (outgoingLinks == null || outgoingLinks.isEmpty()) {
					throw new RuntimeException("Outgoing links is null/empty at " + count);
				}
				inputMatrix[count] = outgoingLinks.split(" ");
			}
			for (int count = 0; count < numberOfPages; count++) {
				pages.add(populatePage(count, inputMatrix, numberOfPages));
			}
			return pages;

		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
	}

	private Page populatePage(final int pageId, final String[][] inputMatrix, final int numberOfPages) {
		List<String> incomingPageIds = new ArrayList<String>();
		List<String> outGoingPageIds = new ArrayList<String>();

		for (int count = 0; count < numberOfPages; count++) {
			if (count == pageId)
				continue;
			if ("1".equals(inputMatrix[pageId][count])) {
				outGoingPageIds.add(String.valueOf(count));
			}
		}

		for (int count = 0; count < numberOfPages; count++) {
			if (count == pageId)
				continue;
			if ("1".equals(inputMatrix[count][pageId])) {
				incomingPageIds.add(String.valueOf(count));
			}
		}

		final Page page = new Page(String.valueOf(pageId));
		page.setIncomingPageIds(incomingPageIds);
		page.setOutGoingPageIds(outGoingPageIds);
		page.setRank(Constants.DEFAULT_PAGE_RANK);

		return page;

	}
}
