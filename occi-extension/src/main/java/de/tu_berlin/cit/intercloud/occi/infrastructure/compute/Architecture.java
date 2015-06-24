package de.tu_berlin.cit.intercloud.occi.infrastructure.compute;

import de.tu_berlin.cit.intercloud.occi.text.TextAttribute;


public class Architecture extends TextAttribute {

	public enum Architectures {
		x86,
		x64
	}

	private Architectures arch;
	
	public Architecture(Architectures arch) {
		super(
				"occi.compute.architecture", // name
				"Enum {x86, x64}", // type
				true, // mutable
				false, // required
				"x86|x64", // pattern
				Architectures.x86.name(), // default
				"CPU Architecture of the instance" // description
				);
		this.arch = arch;
	}
	
	public Architecture(String attr) {
		this(Architectures.x86);
	}
	
	public Architectures getArchitecture() {
		return this.arch;
	}

	@Override
	protected String getValueAsString() {
		return this.arch.name();
	}

	@Override
	protected void parseValue(String value) {
		if(value.equals(Architectures.x86.name()))
			this.arch = Architectures.x86;
		else if(value.equals(Architectures.x64.name()))
			this.arch = Architectures.x64;
		else
			this.arch = Architectures.x86;
	}
	

}
