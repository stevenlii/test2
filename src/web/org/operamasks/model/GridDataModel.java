package org.operamasks.model;

import java.util.ArrayList;
import java.util.List;
import org.operamasks.data.Column;

public class GridDataModel<T>
{
  private int total;
  private List<T> rows = new ArrayList();
  private List<Column> colmodel = new ArrayList();

  public List<Column> getColmodel()
  {
    return this.colmodel;
  }

  public void setColmodel(List<Column> paramList)
  {
    this.colmodel = paramList;
  }

  public List<T> getRows()
  {
    return this.rows;
  }

  public void setRows(List<T> paramList)
  {
    this.rows = paramList;
  }

  public int getTotal()
  {
    return this.total;
  }

  public void setTotal(int paramInt)
  {
    this.total = paramInt;
  }
}