package dto;

import java.util.List;

public class PageResult<T> {
    private List<T> items;
    private int totalCount;
    private int currentPage;
    private int pageSize;

    public PageResult() {}

    public PageResult(List<T> items, int totalCount, int currentPage, int pageSize) {
        this.items = items;
        this.totalCount = totalCount;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        if (pageSize <= 0) return 0;
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    public int getFromIndex() {
        if (totalCount == 0) return 0;
        return (currentPage - 1) * pageSize + 1;
    }

    public int getToIndex() {
        return Math.min(currentPage * pageSize, totalCount);
    }

    public List<T> getItems() { return items; }
    public int getTotalCount() { return totalCount; }
    public int getCurrentPage() { return currentPage; }
    public int getPageSize() { return pageSize; }

    public void setItems(List<T> items) { this.items = items; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}
