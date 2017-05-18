package com.example.superlista;



import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Supermercado;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FragmentSupers extends Fragment {


    private ListView listViewSuper = null;
    private ArrayList<Supermercado> arrayItems = null;
    private List<Supermercado> supermercados;
    private ListAdapterSupermercado adapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_supers, container, false);

        listViewSuper = (ListView) view.findViewById(R.id.lvSuper);
        arrayItems = new ArrayList<>();
        setData();


        return view;
    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.item_supers);
    }


    private void setData(){

        supermercados = SuperListaDbManager.getInstance().getAllSupermercados();

        for (Supermercado supermercado: supermercados) {

            if(Objects.equals(supermercado.getNombre(), "La Gallega")){
                arrayItems.add(new Supermercado(supermercado.getNombre() , "Sucursal: " + supermercado.getSucursal() , R.mipmap.logo_la_gallega));

            }else if(Objects.equals(supermercado.getNombre(), "Coto")){
                arrayItems.add(new Supermercado(supermercado.getNombre() , "Sucursal: " + supermercado.getSucursal() , R.mipmap.logo_coto));

            }else if(Objects.equals(supermercado.getNombre(), "Carrefour")){
                arrayItems.add(new Supermercado(supermercado.getNombre() , "Sucursal: " + supermercado.getSucursal() , R.mipmap.logo_carrefour));

            }else{
                arrayItems.add(new Supermercado(supermercado.getNombre() , "Sucursal: " , R.drawable.ic_store_black_24dp));
            }

        }

        adapter = new ListAdapterSupermercado(getContext(), arrayItems);
        listViewSuper.setAdapter(adapter);

    }






    private class ListAdapterSupermercado extends BaseAdapter {

        private ArrayList<Supermercado> supermercadoArrayList;
        private Context context;
        private LayoutInflater layoutInflater;


        private ListAdapterSupermercado(Context context, ArrayList<Supermercado> supermercadoArrayList) {
            this.context = context;
            this.supermercadoArrayList = supermercadoArrayList;
        }

        @Override
        public int getCount() {
            return supermercadoArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return supermercadoArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vistaItem = layoutInflater.inflate(R.layout.list_supers, parent, false);

            ImageView imageSuper = (ImageView) vistaItem.findViewById(R.id.imageSuper);
            TextView viewSupermercado = (TextView) vistaItem.findViewById(R.id.viewSupermercado);
            TextView viewSucursal = (TextView) vistaItem.findViewById(R.id.viewSucursal);



            imageSuper.setImageResource(supermercadoArrayList.get(position).getLogo());
            viewSupermercado.setText(supermercadoArrayList.get(position).getNombre());
            viewSucursal.setText(supermercadoArrayList.get(position).getSucursal());

            return vistaItem;
        }



    }




}
