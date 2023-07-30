package f21as.group2.interfaces;

/**
 * Code Reference: F21AS-Lecture_examples GitLab 
 * The Subject part of the Observer pattern. 
 * All classes implementing this interface MUST have these methods.
 */
public interface Subject {
	// To register an observer with this subject
	public void registerObserver(Observer obs);

	// To de-register an observer with this subject
	public void removeObserver(Observer obs);

	// To inform all registered observers that there's been an update
	public void notifyObservers();
}
