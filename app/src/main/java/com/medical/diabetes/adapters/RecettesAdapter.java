package com.medical.diabetes.adapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.diabetes.MainActivity;
import com.medical.diabetes.R;
import com.medical.diabetes.fragments.WebNavigator;
import com.medical.diabetes.models.recettes.recette;
import com.medical.diabetes.models.recettes.recettesHeader;

import java.util.ArrayList;

public class RecettesAdapter extends RecyclerView.Adapter<RecettesAdapter.ParentViewHolder> {
    private ArrayList<recettesHeader> recettes;
    private AlertDialog dialog;

    //
    public class ParentViewHolder extends RecyclerView.ViewHolder {
        View container;

        public ParentViewHolder(View container) {
            super(container);
            this.container = container;
        }
    }

    //
    public class TitleViewHolder extends ParentViewHolder {
        public TitleViewHolder(View container) {
            super(container);
        }
    }

    //
    public class RecetteViewHolder extends ParentViewHolder {
        public RecetteViewHolder(View container) {
            super(container);
        }
    }
    //

    public RecettesAdapter(AlertDialog dialog) {
        this.dialog = dialog;
        this.recettes = new ArrayList<>();
        //Entrèes
        recettes.add(new recettesHeader(recettesHeader.RECETTE_TYPE_ENTREE));
        recettes.add(new recette("Bruschetta au rouget", "entrées/bruschetta au rouget.html"));
        recettes.add(new recette("Caviar d'aubergine a la minute", "entrées/caviar d'aubergine a la minute.html"));
        recettes.add(new recette("Entree bourek annabi viande hachee", "entrées/entree bourek annabi viande hachee et oeuf.html"));
        recettes.add(new recette("Epinards au yaourt", "entrées/epinards au yaourt.html"));
        recettes.add(new recette("Potage de patate douce a la tomate", "entrées/potage de patate douce a la tomate.html"));
        recettes.add(new recette("Taboule a la grenade", "entrées/taboule a la grenade.html"));
        recettes.add(new recette("Tartar de thon au gingembre", "entrées/tartar de thon au gingembre.html"));
        //Plats
        recettes.add(new recettesHeader(recettesHeader.RECETTE_TYPE_PLATS));
        recettes.add(new recette("Coquilles Saint-Jacques", "plats/coquilles-saint-jacques.html"));
        recettes.add(new recette("Couscous de poulet", "plats/couscous-de-poulet.html"));
        recettes.add(new recette("Lasagne de legumes", "plats/lasagne-de-legumes.html"));
        recettes.add(new recette("pain de saumon", "plats/pain-de-saumon.html"));
        recettes.add(new recette("plat lasagnes saumons epinards", "plats/plat-lasagnes-saumons-epinards.html"));
        recettes.add(new recette("plat quiche thon legumes", "plats/plat-quiche-thon-legumes.html"));
        recettes.add(new recette("plat risotto poulet champignons", "plats/plat-risotto-poulet-champignons.html"));
        recettes.add(new recette("plat soupe lentilles corail lait coco", "plats/plat-soupe-lentilles-corail-lait-coco.html"));
        //Desserts
        recettes.add(new recettesHeader(recettesHeader.RECETTE_TYPE_DESSERTS));
        recettes.add(new recette("crepes mille trous", "desserts/crepes mille trous.html"));
        recettes.add(new recette("dessert basboussa amandes", "desserts/dessert basboussa amandes.html"));
        recettes.add(new recette("dessert muffin coeur pate a tartiner", "desserts/dessert muffin coeur pate a tartiner.html"));
        recettes.add(new recette("dessert panna cotta vanille framboise", "desserts/dessert panna cotta vanille framboise.html"));
        recettes.add(new recette("genoise chocolat", "desserts/genoise chocolat.html"));
        recettes.add(new recette("mousse chocolat", "desserts/moussechocolat.html"));
        recettes.add(new recette("tartelettes pommes gingembre", "desserts/tartelettes pommes gingembre.html"));
    }

    @Override
    public int getItemViewType(int position) {
        return (recettes.get(position) instanceof recette) ? 1 : 0;
    }

    /**/

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) return new RecetteViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.recette_model, parent, false));
        return new TitleViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.recettes_header, parent, false));

    }

    /**/

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, int position) {
        //
        holder.setIsRecyclable(false);
        //
        TextView textView;
        View container = holder.container;
        if (holder instanceof TitleViewHolder) {
            textView = container.findViewById(R.id.recettes_type);
            textView.setText(recettes.get(position).recetteType);
        } else {
            final recette recet = (recette) recettes.get(position);
            textView = container.findViewById(R.id.recette_name);
            textView.setText(recet.title);
            if (!textView.hasOnClickListeners()) {
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        MainActivity.setFragmentIntoScreen(new WebNavigator(recet.title, recet.filepath, dialog));
                    }
                });
            }
        }
    }

    /**/

    @Override
    public int getItemCount() {
        return recettes.size();
    }
}//
