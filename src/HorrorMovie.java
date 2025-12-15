/**************************************************
 * @file: HorrorMovie.java
 * @description: Represents a horror movie record from the Kaggle dataset. Comparable based on rating (descending), then title.
 * @author: Ben Martin
 * @date: October 26, 2025
 **************************************************/

public record HorrorMovie(String title, double rating)
        implements Comparable<HorrorMovie>
{

    // Compare movies by rating first (descending), then by title alphabetically
    @Override
    public int compareTo(HorrorMovie other)
    {
        int cmp = Double.compare(other.rating, this.rating); // descending so higher rating first
        if (cmp != 0) return cmp;
        return this.title.compareToIgnoreCase(other.title);  // tiebreaker by title
    }

    // Readable string output
    @Override
    public String toString()
    {
        return String.format("%s - %.1f⭐", title, rating);
    }

    // Equality based only on title
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (!(obj instanceof HorrorMovie other)) return false;
        return this.title.equalsIgnoreCase(other.title);
    }
}