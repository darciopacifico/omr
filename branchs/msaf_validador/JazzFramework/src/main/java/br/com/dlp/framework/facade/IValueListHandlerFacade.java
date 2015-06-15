package br.com.dlp.framework.facade;

import java.rmi.RemoteException;
import java.util.List;

import br.com.dlp.framework.dao.QueryOrder;

public interface IValueListHandlerFacade extends IFacade {

	public void executeFindAll() throws ValueListHandlerException,
			RemoteException;

	public void executeFindAll(QueryOrder[] queryOrders)
			throws ValueListHandlerException, RemoteException;

	public int getPageIndex() throws ValueListHandlerException, RemoteException;

	public List getPage() throws ValueListHandlerException, RemoteException;

	public List getPage(int index) throws ValueListHandlerException,
			RemoteException;

	public List getFirstPage() throws ValueListHandlerException,
			RemoteException;

	public List getLastPage() throws ValueListHandlerException, RemoteException;

	public List getNextPage() throws ValueListHandlerException, RemoteException;

	public List getPreviousPage() throws ValueListHandlerException,
			RemoteException;

	public int getPages() throws ValueListHandlerException, RemoteException;

	public int getPageSize() throws ValueListHandlerException, RemoteException;

	public void setPageSize(int pageSize) throws ValueListHandlerException,
			RemoteException;

}
