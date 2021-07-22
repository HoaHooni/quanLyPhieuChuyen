import { Link } from 'react-router-dom';

function ProductItem(props) {
    const productByInventory = props.productByInventory;
    return (
        <div className="test">
            <Link style={{ padding: "0px" }} className="row py-3 item_product " to={{ pathname: `/products/detail/${props.productByInventory.id}`, state: { productByInventory } }}>
                <div className="col-1 ">{props.index + 1}</div>
                <div className="col-2 ">{productByInventory.code}</div>
                <div className="col-3 ">{productByInventory.name}</div>
                <div className="col-2 ">{productByInventory.price}</div>
                <div className="col-1 ">{productByInventory.numberPro}</div>
                <div className="col-3 ">{productByInventory.categoryName}</div>
            </Link>
        </div>
    );
}

export { ProductItem };
