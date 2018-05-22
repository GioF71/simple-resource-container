package eu.giof71.resourceContainer;

public abstract class ResourceContainerException extends RuntimeException {
	
	private static final long serialVersionUID = 7944665353179623474L;

	ResourceContainerException(String message) {
		super(message);
	}

}
