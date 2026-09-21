# Cocktail Sort: Implementation & Comparison

**Data Structures and Algorithms, Team 7**

This paper explains **Cocktail Sort** (also known as shaker sort or bidirectional bubble sort), analyzes its time complexity, and compares it with other basic sorting algorithms.

## Motivation

Simple algorithms like Bubble Sort are still taught because they're clear and useful for learning, even though more efficient algorithms exist. Cocktail Sort improves on Bubble Sort by traversing the list in both directions, which moves small elements to the front faster and can reduce the number of passes. The paper compares Cocktail Sort with Bubble, Insertion, and Selection Sort on execution time, comparisons, and swaps, to see whether the bidirectional approach gives a real practical benefit.

## How It Works

The algorithm alternates two kinds of pass:

- **Forward pass (left to right):** swaps adjacent out-of-order pairs, pushing the largest unsorted element to the right end.
- **Backward pass (right to left):** does the same in reverse, pushing the smallest unsorted element to the left end.

After each pass the boundary moves inward by one, so elements already in their final positions are never checked again (the "shrinking bounds" optimization). If a full pass makes no swaps, the array is sorted and the algorithm stops early.

The paper walks through this on `{2, 4, 1, 5, 6, 8, 0}`, showing each comparison and swap until it terminates after two rounds.

## Time Complexity

- **Best case: O(n).** On already-sorted input, the first forward pass makes n-1 comparisons, finds no swaps, and exits.
- **Worst case: O(n²).** Using a reverse-sorted array of size 6, the paper counts comparisons per round and derives the total two ways:
  - Gauss pairing: n(n-1)/2
  - Rounds times work per round: (n/2)(2n-3) = n² - 3n/2

  Both reduce to O(n²). A chart plots the counted comparisons against n(n-1)/2 and n².

## Implementation

A Java version uses `start` and `end` boundaries and a `swapped` flag. It runs a forward loop, exits early if nothing was swapped, shrinks `end`, runs a backward loop, then advances `start`.

## Comparison with Other Sorts

- **Bubble Sort:** Cocktail Sort usually needs fewer passes because it sorts in both directions, though each full pass does about twice the comparisons.
- **Selection Sort:** Selection Sort does exactly one swap per pass, while Cocktail Sort may do many.
- **Insertion Sort:** Both are close to O(n) on nearly-sorted data and both are stable, but Insertion Sort generally does fewer comparisons and shifts on typical data. It is usually the better choice for small-to-medium real-world datasets.

## Use Cases

Cocktail Sort suits teaching, small datasets, nearly-sorted lists, and data with small elements stuck near the end (the "turtles" that slow Bubble Sort down). It is a poor fit for large datasets or performance-critical systems, where Insertion, Merge, or Quick Sort are better.

Real-world applications the paper lists:

- Nearly sorted data, such as a sorted list with a few new items appended
- Tiny datasets
- Embedded or memory-constrained systems, since it's in-place with O(1) extra memory and no recursion
- Algorithm visualization
- Small multi-key sorts that need stability
