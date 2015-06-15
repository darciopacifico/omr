package br.com.dlp.framework.transaction;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import br.com.dlp.framework.facade.ICadastroFacade;
import br.com.dlp.framework.facade.J2EECMTFacade;

public class TransactionFactory {
	private static TransactionFactory transactionFactory;

	private TransactionFactory() {
	}

	public static TransactionFactory getInstance() {
		if (transactionFactory == null) {
			transactionFactory = new TransactionFactory();
		}
		return transactionFactory;
	}

	public ITransaction getTransaction(ICadastroFacade cadastroFacade)
			throws TransactionFactoryException {
		ITransaction transaction = null;

		if (cadastroFacade instanceof J2EECMTFacade) {
			try {
				transaction = new J2EECMTTransaction(
						((J2EECMTFacade) cadastroFacade).getSessionContext());
			} catch (EJBException e) {
				throw new TransactionFactoryException(
						"N�o foi possivel cria o gerenciador de transa��es", e);

			} catch (RemoteException e) {
				throw new TransactionFactoryException(
						"N�o foi possivel cria o gerenciador de transa��es", e);
			}
		} else {
			throw new TransactionFactoryException(
					"N�o foi poss�vel determinar o tipo de "
							+ "gerenciador de transa��o para a fachada "
							+ cadastroFacade);
		}

		return transaction;
	}

}
