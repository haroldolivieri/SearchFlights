package com.haroldo.searchforflights

import androidx.recyclerview.widget.DiffUtil
import io.reactivex.Observable

inline fun <T> Observable<List<T>>.diffCallback(
    crossinline itemDiffer: (List<T>, List<T>) -> DiffUtil.Callback
): Observable<Pair<List<T>, DiffUtil.DiffResult?>> {

    val initial: Pair<List<T>, DiffUtil.DiffResult?> = Pair(emptyList(), null)

    return scan(initial) { oldPair, newItems ->
        val callback = itemDiffer(oldPair.first, newItems)
        val result = DiffUtil.calculateDiff(callback, true)

        Pair(newItems, result)
    }.skip(1)//ignores initial value
}
