package it.eng.pkgdiff;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * MasterBean del progetto
 * @author Russo Pasquale
 *
 */

@ManagedBean
@ViewScoped
public class MainBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public String getVersion() {
		return "1.0";
	}
}
