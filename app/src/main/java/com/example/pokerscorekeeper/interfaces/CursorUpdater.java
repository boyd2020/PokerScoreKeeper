package com.example.pokerscorekeeper.interfaces;

import java.util.List;

public interface CursorUpdater<T> {

    void setCursor(List<T> items);
}
