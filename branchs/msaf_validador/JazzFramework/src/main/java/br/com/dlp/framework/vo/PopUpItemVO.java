package br.com.dlp.framework.vo;

public class PopUpItemVO implements IBaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8402526196790318453L;

	private Object value;

	private boolean checked;

	public PopUpItemVO(Object value, boolean checked) {
		this.value = value;
		this.checked = checked;
	}

	public PopUpItemVO(Object value) {
		this(value, false);
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
