package application;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import java.text.DecimalFormat;

public class FormatMoneyNumber {
	// Phương thức áp dụng định dạng tiền tệ cho TableColumn
	public static <T> void applyCurrencyFormat(TableColumn<T, Double> column) {
		DecimalFormat decimalFormat = new DecimalFormat("#,###,###");

		column.setCellFactory(new Callback<TableColumn<T, Double>, TableCell<T, Double>>() {
			@Override
			public TableCell<T, Double> call(TableColumn<T, Double> param) {
				return new TableCell<T, Double>() {
					@Override
					protected void updateItem(Double item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
						} else {
							setText(decimalFormat.format(item));
						}
					}
				};
			}
		});
	}
}
