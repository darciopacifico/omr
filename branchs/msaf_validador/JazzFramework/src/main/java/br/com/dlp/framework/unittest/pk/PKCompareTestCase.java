package br.com.dlp.framework.unittest.pk;

public class PKCompareTestCase {
	public static void main(String[] args) {

		TestePK testePK1 = new TestePK();
		PedidoPK pedidoPK1 = new PedidoPK();

		TestePK testePK2 = new TestePK();
		PedidoPK pedidoPK2 = new PedidoPK();

		pedidoPK1.setId(new Long(2));
		pedidoPK1.setZuba("zubisse");
		testePK1.setId(new Long(1));
		testePK1.setIdPedido(new Long(2));
		testePK1.setPedidoPK(pedidoPK1); // <<-------

		pedidoPK2.setId(new Long(2));
		pedidoPK2.setZuba("zubisse");
		testePK2.setId(new Long(1));
		testePK2.setIdPedido(new Long(2));
		testePK2.setPedidoPK(pedidoPK2); // <<-------

		System.out.println(testePK1.compare(testePK2));

	}
}
