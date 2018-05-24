package eu.giof71.resourceContainer.simple;

import eu.giof71.resourceContainer.absSimple.AbsResourceContainer;

public final class SimpleResourceContainer extends AbsResourceContainer<String> {

	public SimpleResourceContainer() {
		super(t -> t.getSimpleName());
	}
}
