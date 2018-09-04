package com.noneadstask.adapter;

import com.noneadstask.model.Person;

public interface ListClicks {
    public void onFavoriteClick(Person person);
    public void onCommentClick(Person person);
    public void onSearch(String searchText);
}
