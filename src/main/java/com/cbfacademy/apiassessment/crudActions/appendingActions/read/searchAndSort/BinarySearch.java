package com.cbfacademy.apiassessment.crudActions.appendingActions.read.searchAndSort;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cbfacademy.apiassessment.exceptions.ItemNotFoundException;
import com.cbfacademy.apiassessment.model.Watchlist;

// uses binary search to find watchlist object by name. sorted array comes from quicksort watchlist and user input.
@Component
public class BinarySearch {

    private static final Logger log = LoggerFactory.getLogger(BinarySearch.class);

    @Autowired
    private QuicksortWatchlist quicksortWatchlist;

    public BinarySearch(QuicksortWatchlist quicksortWatchlist) {
            this.quicksortWatchlist = quicksortWatchlist;
        }

    public List<Watchlist> binarySearchWatchlist(List<Watchlist> existingWatchlist, String stockName){
        List<Watchlist> sortedWatchlist = quicksortWatchlist.sortAlgo(existingWatchlist);
        List<Watchlist> foundEntries = new ArrayList<>();

        int index = binarySearch(sortedWatchlist, stockName);
        log.info("looking for items containing stockName " + stockName + " in their.");

        if(index == -1){
            log.info(stockName + " cannot be found it our watchlist.");
            log.info("Watchlist entries in binary search.", sortedWatchlist);
            throw new ItemNotFoundException(stockName + "Cannot be found in Watchlist");
        } else {
            log.info(stockName + " found at " + index);
            
            int currentIndex = index;
            while (currentIndex >= 0 && sortedWatchlist.get(currentIndex).getStockName().equalsIgnoreCase(stockName)) {
                foundEntries.add(existingWatchlist.get(currentIndex));
                currentIndex--; 
            }
            // return foundEntries;
            currentIndex = index + 1;
            while(currentIndex < existingWatchlist.size() && sortedWatchlist.get(currentIndex).getStockName().equalsIgnoreCase(stockName)){
                foundEntries.add(existingWatchlist.get(currentIndex));
                currentIndex++;
            }
        }
        return foundEntries;
    }

    private int binarySearch(List<Watchlist> sortedWatchlist, String stockName) {
        log.info("Binary search operation is taking place");
        int start = 0;
        int end = sortedWatchlist.size() - 1;
        String searchName = stockName.toLowerCase();

        while(start <= end) {
            int middle = start + (end - start) / 2;
            String value = sortedWatchlist.get(middle).getStockName().toLowerCase();
            int checkResult = value.compareTo(searchName);
            if(checkResult < 0) {
                start = middle + 1;
            } else if(checkResult > 0){
                end = middle - 1;
            } else {
                return middle;
            }
        }
        return -1;
    }

}
