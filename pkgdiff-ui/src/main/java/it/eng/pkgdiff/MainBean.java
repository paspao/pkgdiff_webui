package it.eng.pkgdiff;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class MainBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public String getVersion() {
		return "0.0.1";
	}
}
