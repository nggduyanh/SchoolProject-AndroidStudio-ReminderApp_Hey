package Interfaces;

import androidx.recyclerview.widget.RecyclerView;

import Adapters.ListCreateViewHolder;

public interface IClickListAdd {

    public void setEnable (ListCreateViewHolder holder, int length);

    public void setColor (int pos);

    public void setIcon (int pos);

}
